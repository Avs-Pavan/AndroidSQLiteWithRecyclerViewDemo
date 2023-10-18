package com.kevin.androidsqlitewithrecyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kevin.androidsqlitewithrecyclerviewdemo.databinding.ActivityMainBinding
import com.kevin.androidsqlitewithrecyclerviewdemo.pojo.Person
import com.kevin.androidsqlitewithrecyclerviewdemo.sqlite.SQLiteHelper

class MainActivity : AppCompatActivity() {


    var editPerson: Person? = null

    // View Binding
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // sqliteHelper instance
    private val sqliteHelper: SQLiteHelper by lazy {
        SQLiteHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private val personList = mutableListOf<Person>()

    private val mAdapter: PersonAdapter by lazy {
        PersonAdapter(personList)
    }

    private fun initViews() {

        binding.recyclerView.adapter = mAdapter

        binding.submitBtn.setOnClickListener {

            if (binding.nameEt.validate() && binding.ageEt.validate() && binding.addressEt.validate() && binding.professionEt.validate()) {
                val name = binding.nameEt.textString()
                val age = binding.ageEt.textInt()
                val profession = binding.professionEt.textString()
                val address = binding.addressEt.textString()

                val person = Person(0, name, age, profession, address)
                addPerson(person)
//                clearFields()
            }

        }
    }

    private fun clearFields() {
        binding.nameEt.setText("")
        binding.ageEt.setText("")
        binding.professionEt.setText("")
        binding.addressEt.setText("")
    }

    private fun addPerson(person: Person) {
        // Add the person to the db
        val id = sqliteHelper.insertOne(person)
        if (id > 0) {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
            personList.add(person)
            mAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show()
        }

    }
}