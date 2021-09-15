package com.project.catsdb

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.catsdb.db.Cats
import com.project.catsdb.listeners.OnAdapterClick

class Adapter(private var listItems: List<Cats>) : RecyclerView.Adapter<Adapter.ListViewHolder>() {

    private var mClickListener: View.OnClickListener? = null
    private var adapterClickListener: OnAdapterClick? = null

    inner class ListViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.id_name_content)
        val age: TextView = itemView.findViewById(R.id.id_age_content)
        val breed: TextView = itemView.findViewById(R.id.id_breed_content)
        val container: ViewGroup = itemView.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val myListItem = listItems[position]
        holder.name.text = myListItem.name
        holder.age.text = myListItem.age.toString()
        holder.breed.text = myListItem.breed
        //TODO click
        holder.container.setOnClickListener {
            adapterClickListener?.getCatsData(myListItem)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listItems: List<Cats>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun getDataFromAdapter(inter: OnAdapterClick) {
        this.adapterClickListener = inter
    }
}