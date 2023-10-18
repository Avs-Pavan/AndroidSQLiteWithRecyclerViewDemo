package com.kevin.androidsqlitewithrecyclerviewdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kevin.androidsqlitewithrecyclerviewdemo.databinding.ActivityMainBinding
import com.kevin.androidsqlitewithrecyclerviewdemo.databinding.PersonRowBinding
import com.kevin.androidsqlitewithrecyclerviewdemo.pojo.Person

class PersonAdapter(private val personList: List<Person>): RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

  inner class PersonViewHolder(val binding: PersonRowBinding): ViewHolder(binding.root)

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

    }
}