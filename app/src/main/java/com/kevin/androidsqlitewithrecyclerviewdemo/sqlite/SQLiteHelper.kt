package com.kevin.androidsqlitewithrecyclerviewdemo.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.kevin.androidsqlitewithrecyclerviewdemo.pojo.Person
import com.kevin.androidsqlitewithrecyclerviewdemo.sqlite.Constants.DATABASE_NAME
import com.kevin.androidsqlitewithrecyclerviewdemo.sqlite.Constants.DATABASE_VERSION
import com.kevin.androidsqlitewithrecyclerviewdemo.sqlite.Constants.TABLE_NAME

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val TAG = "SQLiteHelper"
    }


    override fun onCreate(db: SQLiteDatabase?) {

        // Create table
        val createTable = """
            CREATE TABLE $TABLE_NAME (
            ${Constants.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_NAME} TEXT,
            ${Constants.COLUMN_AGE} INTEGER,
            ${Constants.COLUMN_PROFESSION} TEXT,
            ${Constants.COLUMN_ADDRESS} TEXT
            )
            """.trimIndent()

        // Execute the SQL statement
        db?.execSQL(createTable)

        Log.e(TAG, "onCreate: Table created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        // Drop the table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert data into the table
    fun insertOne(person: Person): Long {
        // Get the database in write mode
        val db = this.writableDatabase

        // Create a map of values
        val values = ContentValues().apply {
            put(Constants.COLUMN_NAME, person.name)
            put(Constants.COLUMN_AGE, person.age)
            put(Constants.COLUMN_PROFESSION, person.profession)
            put(Constants.COLUMN_ADDRESS, person.address)
        }

        // Insert the row
        val id = db.insert(TABLE_NAME, null, values)

        // Close the database connection
        db.close()

        // Return the id of the inserted row
        return id
    }

    // delete data from the table
    fun deleteOne(id: Int): Boolean {
        // Get the database in write mode
        val db = this.writableDatabase

        // Delete the row
        val rows = db.delete(TABLE_NAME, "${Constants.COLUMN_ID} = ?", arrayOf(id.toString()))

        // Close the database connection
        db.close()

        return rows > 0

    }

    // delete multiple rows from the table
    fun deleteMultiple(list: Array<String>): Boolean {
        // Get the database in write mode
        val db = this.writableDatabase

        // Delete the row
        val rows = db.delete(TABLE_NAME, "${Constants.COLUMN_ID} = ?", list)

        // Close the database connection
        db.close()

        return rows > 0

    }

    // Read all data from the table
    @SuppressLint("Range")
    fun readAll(): List<Person> {
        // Get the database in read mode
        val db = this.readableDatabase

        // Create a list to store the data
        val personList = mutableListOf<Person>()

        // Query the database
        val query = "SELECT * FROM $TABLE_NAME"

        // Get the cursor object from the database query
        // store it in a variable if it is null return the empty list
        val cursor = db?.rawQuery(query, null)
            ?: // Return the empty list
            return personList

        // Loop through the cursor object and store the data in the list
        while (cursor.moveToNext()) {
            val person = Person(
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_AGE)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PROFESSION)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ADDRESS))
            )

            // Add the person object to the list
            personList.add(person)
        }

        // close the cursor
        cursor.close()

        // close the database
        db.close()
        return personList
    }


    // Read one row from the table
    @SuppressLint("Range")
    fun readOne(id: Int): Person? {
        // Get the database in read mode
        val db = this.readableDatabase


        // Query the database
        val query = "SELECT * FROM $TABLE_NAME WHERE ${Constants.COLUMN_ID} = ?"

        // Get the cursor object from the database query
        // store it in a variable if it is null return null
        val cursor = db?.rawQuery(query, arrayOf(id.toString()))
            ?: // Return null
            return null

        // Loop through the cursor object and return the data
        while (cursor.moveToNext()) {
            return Person(
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_AGE)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PROFESSION)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ADDRESS))
            )
        }

        // close the cursor
        cursor.close()

        // close the database
        db.close()

        return null
    }

    // Update data in the database
    fun updateOne(person: Person): Boolean {

        // Get the database in write mode
        val db = this.writableDatabase

        // Create a map of values
        val values = ContentValues().apply {
            put(Constants.COLUMN_NAME, person.name)
            put(Constants.COLUMN_AGE, person.age)
            put(Constants.COLUMN_PROFESSION, person.profession)
            put(Constants.COLUMN_ADDRESS, person.address)
        }

        // Update the row
        val rows =
            db.update(
                TABLE_NAME,
                values,
                "${Constants.COLUMN_ID} = ?",
                arrayOf(person.id.toString())
            )

        // Close the database connection
        db.close()

        // Return true if the row was updated else false
        return rows > 0

    }

}