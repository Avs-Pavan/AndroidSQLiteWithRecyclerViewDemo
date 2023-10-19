package com.kevin.androidsqlitewithrecyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kevin.androidsqlitewithrecyclerviewdemo.databinding.ActivityMainBinding
import com.kevin.androidsqlitewithrecyclerviewdemo.pojo.Person
import com.kevin.androidsqlitewithrecyclerviewdemo.sqlite.SQLiteHelper

class MainActivity : AppCompatActivity() {


    private var editPerson: Person? = null
    var isActionMode = false
    var count = 0
    private var isEditMode = false

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
            selectedIds.clear()
            binding.count.text = "$count selected"
            binding.delete.hide()
        }

        binding.submitBtn.setOnClickListener {


            if (binding.nameEt.validate() && binding.ageEt.validate() && binding.addressEt.validate() && binding.professionEt.validate()) {
                val name = binding.nameEt.textString()
                val age = binding.ageEt.textInt()
                val profession = binding.professionEt.textString()
                val address = binding.addressEt.textString()

                if (!isEditMode) {
                    val person = Person(0, name, age, profession, address)
                    addPerson(person)
                    clearFields()
                } else {
                    editPerson?.let {
                        it.name = name
                        it.age = age
                        it.profession = profession
                        it.address = address
                        if (sqliteHelper.updateOne(it)) {
                            Toast.makeText(this, "Person updated successfully", Toast.LENGTH_SHORT)
                                .show()
                            mAdapter.notifyItemChanged(personList.indexOf(it))
                        } else {
                            Toast.makeText(this, "Failed to update person", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                    clearFields()
                    binding.cancelBtn.hide()
                    isEditMode = false
                }
            }

        }

        binding.delete.setOnClickListener {
            if (selectedIds.isNotEmpty()) {

                // create strign array
                val ids = Array(selectedIds.size) { i -> selectedIds[i].toString() }
                var deleted = sqliteHelper.deleteMultiple(ids.joinToString())

                if (deleted) {
                    Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_SHORT).show()
                    restoreList()
                    isActionMode = false
                    mAdapter.setActionMode(isActionMode)

                    binding.toolbar.hide()
                    count = 0
                    selectedIds.clear()
                    binding.count.text = "$count selected"
                    binding.delete.hide()
                } else {
                    Toast.makeText(this, "Failed to delete data", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.cancelBtn.setOnClickListener {
            clearFields()
            binding.cancelBtn.hide()
            editPerson = null
            isEditMode = false
        }
    }

    private fun startEditAction(person: Person) {
        isEditMode = true
        this.editPerson = person
        binding.nameEt.setText(person.name)
        binding.ageEt.setText(person.age.toString())
        binding.professionEt.setText(person.profession)
        binding.addressEt.setText(person.address)
        binding.cancelBtn.show()
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