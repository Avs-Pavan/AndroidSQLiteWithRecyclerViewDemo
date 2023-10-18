package com.kevin.androidsqlitewithrecyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kevin.androidsqlitewithrecyclerviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // View Binding
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}