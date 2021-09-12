package com.project.catsdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private var _catsListFragment: MainListOfCats? = null
    private val catsListFragment get() = _catsListFragment

    private var _addNewFragment: AddNew? = null
    private val addNewFragment get() = _addNewFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}