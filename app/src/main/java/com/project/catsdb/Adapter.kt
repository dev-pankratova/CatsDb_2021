package com.project.catsdb

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.catsdb.databinding.ListItemBinding
import com.project.catsdb.db.Cats
import com.project.catsdb.listeners.OnItemClickListener

class Adapter(private var listItems: List<Cats>) : RecyclerView.Adapter<Adapter.ListViewHolder>() {

    private var mClickListener: View.OnClickListener? = null

    private var listener: OnItemClickListener? = null

    inner class ListViewHolder(/*private val itemView: View*/ val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.idNameContent
        val age: TextView = binding.idAgeContent
        val breed: TextView = binding.idBreedContent
    /*val name: TextView = itemView.findViewById(R.id.id_name_content)
        val age: TextView = itemView.findViewById(R.id.id_age_content)
        val breed: TextView = itemView.findViewById(R.id.id_breed_content)*/
        //val container: ViewGroup = itemView.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
        /*val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(itemView)*/
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val myListItem = listItems[position]
        with(holder.binding) {
            holder.name.text = myListItem.name
            holder.age.text = myListItem.age.toString()
            holder.breed.text = myListItem.breed
        }

        if(listener != null){
            holder.itemView.setOnClickListener {
                listener!!.onVariantClick(myListItem)
            }
        }
        /*holder.container.setOnClickListener {
            adapterClickListener?.getCatsData(myListItem)
        }*/
    }

    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listItems: List<Cats>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }
}