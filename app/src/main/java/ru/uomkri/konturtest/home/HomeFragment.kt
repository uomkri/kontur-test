package ru.uomkri.konturtest.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import ru.uomkri.konturtest.databinding.FragmentHomeBinding
import ru.uomkri.konturtest.db.UserDatabase
import ru.uomkri.konturtest.home.list.UsersListAdapter
import ru.uomkri.konturtest.utils.CurrentTimeProvider
import ru.uomkri.konturtest.utils.RecyclerItemClickListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var listAdapter: UsersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application, CurrentTimeProvider())

        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val prefs = requireActivity().getSharedPreferences("time", Context.MODE_PRIVATE)
        val lastUpdate = prefs.getLong("lastUpdate", 0)

        if (viewModel.isUpdateNeeded(lastUpdate)) {
            viewModel.refreshDataFromRepository(requireActivity())
        }

        requireActivity().actionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        requireActivity().searchView.visibility = View.VISIBLE

        requireActivity().searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    when {
                        newText != null && newText.isEmpty() -> {
                            listAdapter.refreshList(viewModel.users.value!!)
                        }
                        newText != null && newText.isNotEmpty() && newText.first().isDigit() -> {
                            val filtered = viewModel.filterByPhone(newText)
                            listAdapter.refreshList(filtered)
                        }
                        newText != null && newText.isNotEmpty() && newText.first().isLetter() -> {
                            val filtered = viewModel.filterByName(newText)
                            listAdapter.refreshList(filtered)
                        }
                    }

                    return false
                }

            }
        )

        viewModel.hasRequestFailed.observe(viewLifecycleOwner, Observer {
            if (it != null && it == true) {
                Snackbar.make(requireView(), "Нет подключения к сети", Snackbar.LENGTH_SHORT).show()
                viewModel.resetRequestStatus()
            }
        })

        viewModel.users.observe(viewLifecycleOwner, Observer { list ->
            if (list != null) {

                Log.e("das", list.toString())

                val listLayoutManager = LinearLayoutManager(context)
                listAdapter = UsersListAdapter(list)

                binding.recyclerView.apply {
                    setHasFixedSize(false)
                    layoutManager = listLayoutManager
                    adapter = listAdapter

                    val divider = DividerItemDecoration(context, listLayoutManager.orientation)
                    addItemDecoration(divider)
                }
            }
        })

        viewModel.shouldProgressBeShown.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        binding.swipeContainer.setOnRefreshListener {
            listAdapter.clear()
            viewModel.refreshDataFromRepository(requireActivity())
            swipeContainer.isRefreshing = false
        }

        binding.recyclerView.addOnItemTouchListener(RecyclerItemClickListener(binding.recyclerView,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    view.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                            viewModel.users.value?.get(
                                position
                            )?.id
                        )
                    )
                }
            }
        ))

        return binding.root
    }
}