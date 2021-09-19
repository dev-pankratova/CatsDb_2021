package com.project.catsdb

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.catsdb.CatsViewModel.Companion.MODE_CURSOR
import com.project.catsdb.CatsViewModel.Companion.MODE_ROOM
import com.project.catsdb.databinding.AddNewFragmentBinding
import com.project.catsdb.db.AppDatabase
import com.project.catsdb.db.Cats
import com.project.catsdb.db.CatsDao
import com.project.catsdb.db.SQLiteOpenHelper
import java.io.Serializable

class AddNew : Fragment() {

    private val viewModel: CatsViewModel by viewModels()

    private var binding: AddNewFragmentBinding? = null
    private var roomDataBase: AppDatabase? = null
    private var catsDao: CatsDao? = null
    private var item: Serializable? = null

    private var sqlOpenHelper: SQLiteOpenHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddNewFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = arguments?.getSerializable("CatsItem")

        initOptionsMenu()

        initDataBaseByMode()

        setActionBtnClickListener()
        setButtonName()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.item1).isVisible = false
        menu.findItem(R.id.item2).isVisible = false
        menu.findItem(R.id.item3).isVisible = false
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initOptionsMenu() {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun initDataBaseByMode() {
        viewModel.modeDb1.observe(viewLifecycleOwner) {
            when (it) {
                MODE_ROOM -> {
                    initRoomDataBase()
                }
                MODE_CURSOR -> {
                    initSQLDataBase()
                }
            }
        }
    }

    private fun initRoomDataBase() {
        roomDataBase = (activity?.application as App).getInstance()?.getDatabase()
        catsDao = roomDataBase?.catsDao()
    }

    private fun initSQLDataBase() {
        sqlOpenHelper = activity?.applicationContext?.let { SQLiteOpenHelper(it) }
    }

    private fun setActionBtnClickListener() {
        binding?.addButton?.setOnClickListener {
            if (item != null) {
                updateToDb((item as Cats).id)
                activity?.onBackPressed()
            } else addToDB()
        }
    }

    private fun setButtonName() {
        if (item != null) {
            fillDataForUpdate(item as Cats)
            setUpdateName()
        } else setAddName()
    }

    private fun fillDataForUpdate(item: Cats) {
        binding?.idNameET?.setText(item.name)
        binding?.idAgeET?.setText(item.age.toString())
        binding?.idBreedET?.setText(item.breed)
    }

    private fun setUpdateName() {
        binding?.addButton?.text = resources.getString(R.string.update)
    }

    private fun setAddName() {
        binding?.addButton?.text = resources.getString(R.string.add)
    }

    private fun addToDB() {
        val newCat = Cats()

        val name = binding?.idNameET?.text.toString()
        val age = binding?.idAgeET?.text.toString()
        val breed = binding?.idBreedET?.text.toString()

        newCat.name = name
        if (age.isNotEmpty()) newCat.age = age.toInt()
        newCat.breed = breed

        when (viewModel.getModeDb()) {
            MODE_ROOM -> addRecordToRoomDataBase(newCat)
            MODE_CURSOR -> addRecordToSQLDataBase(newCat)
        }
    }

    private fun addRecordToRoomDataBase(cat: Cats) {
        if (cat.name?.trim() != "" && cat.age.toString().trim() != "" && cat.age.toString().trim() != "null" && cat.breed?.trim() != "") {
            catsDao?.insert(cat)
            clearFields()
            showToastRecordSaved("Room")
            activity?.onBackPressed()
        } else showEmptyDataToast()
    }

    private fun addRecordToSQLDataBase(cat: Cats) {
        val status = sqlOpenHelper?.saveRecord(cat)
        if (status != null) {
            when (status) {
                "blank" -> showEmptyDataToast()
                "success" -> {
                    showToastRecordSaved("SQLite")
                    clearFields()
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun updateToDb(catId: Int?) {
        val name = binding?.idNameET?.text.toString()
        val age = binding?.idAgeET?.text.toString()
        val breed = binding?.idBreedET?.text.toString()

        val newCat = Cats()

        newCat.id = catId
        newCat.name = name
        newCat.age = age.toInt()
        newCat.breed = breed
        when (viewModel.getModeDb()) {
            MODE_ROOM -> updateRoomDataBase(newCat)
            MODE_CURSOR -> updateSQLDataBase(newCat)
        }
        activity?.onBackPressed()
    }

    private fun updateRoomDataBase(cat: Cats?) {
        catsDao?.update(cat)
        showToastRecordUpdate()
    }

    private fun updateSQLDataBase(cat: Cats?) {
        val status = cat?.let { sqlOpenHelper?.updateCatFromSQL(it) }
        if (status != null) {
            if(status > -1) {
                showToastRecordUpdate()
            }
        }
    }

    private fun clearFields() {
        binding?.idNameET?.text?.clear()
        binding?.idAgeET?.text?.clear()
        binding?.idBreedET?.text?.clear()
    }

    private fun showToastRecordSaved(text: String) {
        Toast.makeText(activity?.applicationContext,"record saved in the $text",Toast.LENGTH_LONG).show()
    }

    private fun showEmptyDataToast() {
        Toast.makeText(activity?.applicationContext, "name or age or breed cannot be blank", Toast.LENGTH_LONG).show()
    }

    private fun showToastRecordUpdate() {
        Toast.makeText(activity?.applicationContext,"record update",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(cat: Cats? = null): AddNew {
            val fragment = AddNew()
            val argument = bundleOf("CatsItem" to cat)
            return fragment.apply {
                arguments = argument
            }
        }
    }
}