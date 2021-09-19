package com.project.catsdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.catsdb.databinding.ListOfCatsFragmentBinding
import com.project.catsdb.db.AppDatabase
import com.project.catsdb.db.Cats
import com.project.catsdb.db.CatsDao
import com.project.catsdb.listeners.OnAddNewCatClickListener
import com.project.catsdb.listeners.OnItemClickListener
import com.project.catsdb.listeners.OnSendClickDataToActivity
import android.content.Context.MODE_PRIVATE

import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.project.catsdb.CatsViewModel.Companion.MODE_CURSOR
import com.project.catsdb.CatsViewModel.Companion.MODE_ROOM
import com.project.catsdb.adapter.Adapter
import com.project.catsdb.db.SQLiteOpenHelper

class MainListOfCats : Fragment() {

    private val viewModel: CatsViewModel by viewModels()

    private var dbRoom: AppDatabase? = null
    private var catsDao: CatsDao? = null

    var sqlOpenHelper: SQLiteOpenHelper? = null

    private var binding: ListOfCatsFragmentBinding? = null
    private var floatBtnInterface: OnAddNewCatClickListener? = null
    private var sendDataInterface: OnSendClickDataToActivity? = null
    private var sortedListByPref: List<Cats>? = null
    private var watchAdapter: Adapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListOfCatsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOptionsMenu()
        setSwitchBetweenDbListener()
        setActionFloatBtn()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).currentFragment = (activity as MainActivity).catsListFragment
    }

    private fun initOptionsMenu() {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
    }

    private fun fillAdapter(catsList: List<Cats>?) {
        watchAdapter = catsList?.let { Adapter(it) }
        binding?.recycler?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = watchAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        setItemClickListener()
    }

    private fun setItemClickListener() {
        watchAdapter?.setListener(object : OnItemClickListener {
            override fun onVariantClick(model: Cats?) {
                if (model != null) {
                    sendDataInterface?.sendData(model)
                }
            }
        })
    }

    fun setInterface(inter: OnAddNewCatClickListener) {
        this.floatBtnInterface = inter
    }

    fun sendDataToActivity(inter: OnSendClickDataToActivity) {
        this.sendDataInterface = inter
    }

    private fun setActionFloatBtn() {
        binding?.floatBtn?.setOnClickListener { openAddNewFragment() }
    }

    private fun openAddNewFragment() {
        floatBtnInterface?.btnIsClicked(true)
    }

    private fun setClickClearBtn(mode: String) {
        binding?.buttonClear?.setOnClickListener {
            clearDb(mode)
            clearRecyclerView() }
    }

    private fun clearDb(mode: String) {
        when (mode) {
            MODE_ROOM -> clearRoomDataBase()
            MODE_CURSOR -> clearSQLDataBase()
        }
    }

    private fun clearRoomDataBase() {
        catsDao?.deleteAll()
        showToastClearDb("Room")
    }

    private fun clearSQLDataBase() {
        sqlOpenHelper?.clearDataBase()
        showToastClearDb("SQLite")
    }

    private fun clearRecyclerView() {
        val watchAdapter = Adapter(listOf())
        binding?.recycler?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = watchAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initSort(mode: String) {
        val preferenceIntent = activity?.intent
                when (preferenceIntent?.dataString) {
                    "sortByName" -> setSortedListByName(mode)
                    "sortByAge" -> setSortedListByAge(mode)
                    "sortByBreed" -> setSortedListByBreed(mode)
                }
            }


    private fun setSortedListByName(mode: String?) {
        when (mode) {
            MODE_ROOM -> sortedListByPref = catsDao?.getSortByName()
            MODE_CURSOR -> sortedListByPref = sqlOpenHelper?.getSortByName()
        }
        fillAdapter(sortedListByPref)
    }

    private fun setSortedListByAge(mode: String?) {
        when (mode) {
            MODE_ROOM -> sortedListByPref = catsDao?.getSortByAge()
            MODE_CURSOR -> sortedListByPref = sqlOpenHelper?.getSortByAge()
        }
        fillAdapter(sortedListByPref)
    }

    private fun setSortedListByBreed(mode: String?) {
        when (mode) {
            MODE_ROOM -> sortedListByPref = catsDao?.getSortByBreed()
            MODE_CURSOR -> sortedListByPref = sqlOpenHelper?.getSortByBreed()
        }
        fillAdapter(sortedListByPref)
    }

    private fun setSwitchBetweenDbListener() {
        viewModel.modeDb1.observe(viewLifecycleOwner) {
            when (it) {
                MODE_ROOM -> {
                    initRoomDataBase()
                    val catsList: List<Cats>? = catsDao?.getAll()
                    fillAdapter(catsList)
                    setClickClearBtn(MODE_ROOM)
                }
                MODE_CURSOR -> {
                    initCursorDataBase()
                    val catsList: List<Cats>? = sqlOpenHelper?.getListOfTopics()
                    fillAdapter(catsList)
                    setClickClearBtn(MODE_CURSOR)
                }
            }
            showViews()
            initSort(it)
        }
    }

    private fun showViews() {
        binding?.modeInfoTxt?.visibility = View.GONE
        binding?.recycler?.visibility = View.VISIBLE
        binding?.layoutForButtons?.visibility = View.VISIBLE
    }

    private fun initRoomDataBase() {
        dbRoom = (activity?.application as App).getInstance()?.getDatabase()
        catsDao = dbRoom?.catsDao()
    }

    private fun initCursorDataBase() {
        sqlOpenHelper = activity?.applicationContext?.let { SQLiteOpenHelper(it) }
    }

    private fun showToastClearDb(text: String) {
        Toast.makeText(activity?.applicationContext,"$text DB is cleared", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): MainListOfCats {
            return MainListOfCats()
        }
    }
}