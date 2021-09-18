package com.project.catsdb

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.catsdb.databinding.ListItemBinding
import com.project.catsdb.db.Cats
import com.project.catsdb.listeners.OnItemClickListener

class Adapter(private var listItems: List<Cats>) : RecyclerView.Adapter<Adapter.ListViewHolder>() {

    private var listener: OnItemClickListener? = null

    inner class ListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.idNameContent
        val age: TextView = binding.idAgeContent
        val breed: TextView = binding.idBreedContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val myListItem = listItems[position]
        //with(holder.binding) {
            holder.name.text = myListItem.name
            holder.age.text = myListItem.age.toString()
            holder.breed.text = myListItem.breed
        //}

        if(listener != null){
            holder.itemView.setOnClickListener {
                listener!!.onVariantClick(myListItem)
            }
        }
    }

    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}