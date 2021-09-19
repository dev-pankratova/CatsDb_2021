package com.project.catsdb

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.project.catsdb.CatsViewModel.Companion.MODE_CURSOR
import com.project.catsdb.CatsViewModel.Companion.MODE_ROOM
import com.project.catsdb.db.Cats
import com.project.catsdb.listeners.OnAddNewCatClickListener
import com.project.catsdb.listeners.OnAddNewCatInDbListener
import com.project.catsdb.listeners.OnSendClickDataToActivity
import com.project.catsdb.settings.SortingFragment

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels()

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
        _addNewFragment = AddNew.newInstance()

        openMainListOfCats()
        setFloatingBtnAction()
        addNewCatInDb()
        getDataAndSendToUpdate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.item1) {
            sortingFragment?.let { attachFragment(it) }
        }
        if (id == R.id.item2) {
            viewModel.setMode(MODE_CURSOR)
            viewModel.setModeDb(MODE_CURSOR)
            item.isChecked = true
            Toast.makeText(applicationContext, "Включем режим Cursor", LENGTH_SHORT).show()
        }
        if (id == R.id.item3) {
            viewModel.setMode(MODE_ROOM)
            viewModel.setModeDb(MODE_ROOM)
            item.isChecked = true
            Toast.makeText(applicationContext, "Включем режим Room", LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openMainListOfCats() {
        _catsListFragment = MainListOfCats.newInstance()
        catsListFragment?.let { attachFragment(it) }
    }

    private fun setFloatingBtnAction() {
        catsListFragment?.setInterface(object : OnAddNewCatClickListener {
            override fun btnIsClicked(bool: Boolean) {
                if (bool) addNewFragment?.let { attachFragment(AddNew.newInstance()) }
            }
        })
    }

    private fun addNewCatInDb() {
        addNewFragment?.setInterface(object : OnAddNewCatInDbListener {
            override fun addInDb(bool: Boolean) {
                if (bool) catsListFragment?.let { attachFragment(it) }
            }
        })
    }

    private fun getDataAndSendToUpdate() {
        catsListFragment?.sendDataToActivity(object : OnSendClickDataToActivity {
            override fun sendData(cat: Cats) {
                addNewFragment?.let { attachFragment(AddNew.newInstance(cat)) }
            }
        })
    }

    private fun attachFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }
    }
}