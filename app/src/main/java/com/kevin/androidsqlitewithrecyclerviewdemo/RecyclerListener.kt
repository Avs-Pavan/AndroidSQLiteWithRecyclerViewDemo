package com.kevin.androidsqlitewithrecyclerviewdemo

interface RecyclerListener {
    fun onLongPress(item: Int)
    fun onClick(personId: Int, position: Int)
}