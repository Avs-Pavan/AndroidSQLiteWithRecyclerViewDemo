package com.kevin.androidsqlitewithrecyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kevin.androidsqlitewithrecyclerviewdemo.databinding.ActivityMainBinding
import com.kevin.androidsqlitewithrecyclerviewdemo.pojo.Person
import com.kevin.androidsqlitewithrecyclerviewdemo.sqlite.SQLiteHelper

class MainActivity : AppCompatActivity() {


    var editPerson: Person? = null
    var isActionMode = false
    var count = 0

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
    private val selectedIds = mutableListOf<Int>()

    private lateinit var mAdapter: PersonAdapter

    private fun initViews() {

        mAdapter = PersonAdapter(personList, isActionMode, object : RecyclerListener {
            override fun onLongPress(item: Int) {
                if (!isActionMode) {
                    isActionMode = true
                    mAdapter.setActionMode(this@MainActivity.isActionMode)
                    binding.toolbar.visibility = android.view.View.VISIBLE
                }
            }

            override fun onClick(item: Int, position: Int) {

                if (isActionMode) {
                    if (selectedIds.contains(item)) {
                        selectedIds.remove(item)
                        count--
                        binding.count.text = "$count selected"
                        if (count < 1) {
                            binding.delete.hide()
                        } else
                            binding.delete.show()
                    } else {
                        selectedIds.add(item)
                        count++
                        binding.count.text = "$count selected"
                        if (count > 0) {
                            binding.delete.show()
                        } else
                            binding.delete.hide()
                    }
                } else {
                    // start edit action
                    startEditAction(personList[position])
                }

            }

        })

        binding.recyclerView.adapter = mAdapter
        restoreList()

        binding.backBtn.setOnClickListener {
            isActionMode = false
            mAdapter.setActionMode(isActionMode)
            binding.toolbar.hide()
            count = 0

        }

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

        binding.delete.setOnClickListener {
            if (selectedIds.isNotEmpty()) {

               // create strign array
                val ids = Array<String>(selectedIds.size) { i -> selectedIds[i].toString() }
                sqliteHelper.deleteMultiple(ids)

                restoreList()
                isActionMode = false
                mAdapter.setActionMode(isActionMode)

                binding.toolbar.hide()
                count = 0
            }
        }
    }

    private fun startEditAction(person: Person) {

    }

    private fun restoreList() {
        personList.clear()
        sqliteHelper.readAll().forEach {
            personList.add(it)
        }
        mAdapter.notifyDataSetChanged()
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