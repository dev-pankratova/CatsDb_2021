package com.project.catsdb

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.catsdb.db.AppDatabase
import com.project.catsdb.db.Cats
import com.project.catsdb.db.CatsDao
import com.project.catsdb.listeners.OnAdapterClick
import com.project.catsdb.listeners.OnAddNewCatClickListener
import com.project.catsdb.listeners.OnAddNewCatInDbListener
import com.project.catsdb.settings.SortingFragment

class MainActivity : AppCompatActivity() {

    private var _catsListFragment: MainListOfCats? = null
    private val catsListFragment get() = _catsListFragment

    private var _addNewFragment: AddNew? = null
    private val addNewFragment get() = _addNewFragment

    private var _sortingFragment: SortingFragment? = null
    private val sortingFragment get() = _sortingFragment

    private var adapterCats: Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _sortingFragment = SortingFragment.newInstance()
        _addNewFragment = AddNew.newInstance()
        openMainListOfCats()
        setFloatingBtnAction()
        addNewCatInDb()
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
        return super.onOptionsItemSelected(item)
    }

    private fun openMainListOfCats() {
        _catsListFragment = MainListOfCats.newInstance()

        catsListFragment?.watchAdapter?.getDataFromAdapter(object : OnAdapterClick {
            override fun getCatsData(cat: Cats) {
                attachFragment(AddNew.newInstance(cat))
                //addNewFragment?.let { attachFragment(it) }
            }
        })

        catsListFragment?.let { attachFragment(it) }
    }

    private fun setFloatingBtnAction() {
        catsListFragment?.setInterface(object : OnAddNewCatClickListener {
            override fun btnIsClicked(bool: Boolean) {
                if (bool) addNewFragment?.let { attachFragment(it) }
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

    private fun attachFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }
    }
}