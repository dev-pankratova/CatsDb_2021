package com.project.catsdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.catsdb.db.dao.Table

class Adapter(val listItems: MutableList<Table>) : RecyclerView.Adapter<Adapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.id_name_content)
        val age: TextView = itemView.findViewById(R.id.id_age_content)
        val breed: TextView = itemView.findViewById(R.id.id_breed_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        val holder = ListViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val myListItem = listItems[position]
        holder.name.text = myListItem.name
        holder.age.text = myListItem.age.toString()
        holder.breed.text = myListItem.breed
    }
}