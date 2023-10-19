package com.kevin.androidsqlitewithrecyclerviewdemo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kevin.androidsqlitewithrecyclerviewdemo.databinding.PersonRowBinding
import com.kevin.androidsqlitewithrecyclerviewdemo.pojo.Person

class PersonAdapter(
    private val personList: List<Person>,
    private var isActionMode: Boolean,
    private val listener: RecyclerListener
) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(val binding: PersonRowBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = PersonRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.binding.name.text = person.name
        holder.binding.age.text = person.age.toString()
        holder.binding.profession.text = person.profession
        holder.binding.address.text = person.address
        if (isActionMode) {
            holder.binding.linearLayoutCheckBox.visibility = ViewGroup.VISIBLE
        } else{
            holder.binding.checkBox.isChecked = false
            holder.binding.linearLayoutCheckBox.visibility = ViewGroup.GONE
        }

        holder.binding.cardView.setOnLongClickListener {
            listener.onLongPress(position)
            true
        }

        holder.binding.cardView.setOnClickListener {
            with(holder.binding.checkBox) {
                isChecked = !isChecked
            }
            listener.onClick(person.id, position)
            true
        }

    }


    fun setActionMode(boolean: Boolean) {
        this.isActionMode = boolean
        notifyDataSetChanged()
    }
}