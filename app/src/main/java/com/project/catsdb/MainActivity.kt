package com.project.catsdb

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.project.catsdb.databinding.ActivityMainBinding
import com.project.catsdb.settings.SortingFragment

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private var _catsListFragment: MainListOfCats? = null
    private val catsListFragment get() = _catsListFragment

    private var _addNewFragment: AddNew? = null
    private val addNewFragment get() = _addNewFragment

    private var _sortingFragment: SortingFragment? = null
    private val sortingFragment get() = _sortingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _sortingFragment = SortingFragment.newInstance()
        openMainListOfCats()
        setFloatingBtnAction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.item1) {
            sortingFragment?.let { attachFragment(it) }
            //val intent = Intent(this, SortingActivity::class.java)
            //startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openMainListOfCats() {
        _catsListFragment = MainListOfCats.newInstance()
        catsListFragment?.let { attachFragment(it) }
    }

    private fun setFloatingBtnAction() {
        binding
    }

    private fun attachFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }
}