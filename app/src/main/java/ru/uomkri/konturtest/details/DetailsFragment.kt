package ru.uomkri.konturtest.details

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.activity_main.*
import ru.uomkri.konturtest.databinding.FragmentDetailsBinding
import ru.uomkri.konturtest.db.UserDatabase


class DetailsFragment : Fragment() {

    private val PERMISSION_REQUEST_CODE: Int = 2

    private lateinit var binding: FragmentDetailsBinding

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDao
        val viewModelFactory = DetailsViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)

        val userId = args.userId
        viewModel.getUserById(userId!!)

        requireActivity().actionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        requireActivity().searchView.visibility = View.GONE

        requireActivity().toolbar.setNavigationOnClickListener {
            binding.root.findNavController().popBackStack()
        }

        viewModel.selectedUser.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val period = viewModel.selectedUser.value!!.educationPeriod

                binding.userName.text = viewModel.selectedUser.value!!.name
                binding.userPhone.text = viewModel.selectedUser.value!!.phone
                binding.userEducationPeriod.text = viewModel.renderEducationPeriod(period)
                binding.userTemperament.text = viewModel.selectedUser.value!!.temperament
                binding.userBiography.text = viewModel.selectedUser.value!!.biography

                binding.userPhone.setOnClickListener {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(Manifest.permission.CALL_PHONE),
                            PERMISSION_REQUEST_CODE
                        )

                    } else {
                        val ph = Intent(
                            Intent.ACTION_CALL,
                            Uri.parse("tel:${viewModel.selectedUser.value!!.phone}")
                        )
                        startActivity(ph)
                    }
                }
            }
        })



        return binding.root
    }
}