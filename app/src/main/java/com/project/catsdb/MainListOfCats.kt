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
import com.project.catsdb.db.CatsDao
import com.project.catsdb.listeners.OnAddNewCatClickListener

class MainListOfCats : Fragment() {

    private var db: AppDatabase? = null
    private var catsDao: CatsDao? = null

    private var binding: ListOfCatsFragmentBinding? = null
    private var floatBtnInterface: OnAddNewCatClickListener? = null

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

        db = (activity?.application as App).getInstance()?.getDatabase()
        catsDao = db?.catsDao()

        setActionFloatBtn()
        setClickClearBtn()
    }

    override fun onResume() {
        super.onResume()
        fillAdapter()
    }

    private fun fillAdapter() {
        val catsList = catsDao?.getAll()

        val watchAdapter = catsList?.let { Adapter(it) }
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

    fun setInterface(inter: OnAddNewCatClickListener) {
        this.floatBtnInterface = inter
    }

    private fun setActionFloatBtn() {
        binding?.floatBtn?.setOnClickListener { openAddNewFragment() }
    }

    private fun openAddNewFragment() {
        floatBtnInterface?.btnIsClicked(true)
    }

    private fun setClickClearBtn() {
        binding?.buttonClear?.setOnClickListener { clearDb() }
    }

    //TODO clearDb
    private fun clearDb() {
        catsDao?.deleteAll()

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

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): MainListOfCats {
            val fragment = MainListOfCats()

            return fragment
        }
    }
}