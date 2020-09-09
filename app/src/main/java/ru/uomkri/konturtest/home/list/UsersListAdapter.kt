package ru.uomkri.konturtest.home.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.uomkri.konturtest.R
import ru.uomkri.konturtest.net.NetUser

class UsersListAdapter(private val data: List<NetUser>) : RecyclerView.Adapter<ViewHolder>() {

    private val mutableData = data.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mutableData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mutableData[position]
        holder.userName.text = item.name
        holder.userHeight.text = item.height.toString()
        holder.userPhone.text = item.phone
    }

    fun clear() {
        mutableData.clear()
        notifyDataSetChanged()
    }

    fun refreshList(list: List<NetUser>) {
        mutableData.clear()
        mutableData.addAll(list)
        notifyDataSetChanged()
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userName: TextView = itemView.findViewById(R.id.userName)
    val userPhone: TextView = itemView.findViewById(R.id.userPhone)
    val userHeight: TextView = itemView.findViewById(R.id.userHeight)
}