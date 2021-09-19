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
import com.project.catsdb.listeners.OnAddNewCatInDbListener
import java.io.Serializable

class AddNew : Fragment() {

    private val viewModel: CatsViewModel by viewModels()

    private var binding: AddNewFragmentBinding? = null
    private var roomDataBase: AppDatabase? = null
    private var catsDao: CatsDao? = null
    private var addNewCatListener: OnAddNewCatInDbListener? = null
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
                addNewCatListener?.addInDb(true)
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
    var id1: Int = 0
    private fun addToDB() {
        val newCat = Cats()

        val name = binding?.idNameET?.text.toString()
        val age = binding?.idAgeET?.text.toString()
        val breed = binding?.idBreedET?.text.toString()

        newCat.id = id1 ++
        newCat.name = name
        newCat.age = age.toInt()
        newCat.breed = breed

        when (viewModel.getModeDb()) {
            MODE_ROOM -> addRecordToRoomDataBase(newCat)
            MODE_CURSOR -> addRecordToSQLDataBase(newCat)
        }

        addNewCatListener?.addInDb(true)
        activity?.onBackPressed()
    }

    private fun addRecordToRoomDataBase(cat: Cats) {
        catsDao?.insert(cat)
        // TODO Toast if input data is blank
    }

    private fun addRecordToSQLDataBase(cat: Cats) {
        val status = sqlOpenHelper?.saveRecord(cat)
        if (status != null) {
            when (status) {
                "blank" -> Toast.makeText(activity?.applicationContext, "name or age or breed cannot be blank", Toast.LENGTH_LONG).show()
                "success" -> {
                    Toast.makeText(activity?.applicationContext,"record save",Toast.LENGTH_LONG).show()
                    clearFields()
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

        addNewCatListener?.addInDb(true)
        activity?.onBackPressed()
    }

    private fun updateRoomDataBase(cat: Cats?) {
        catsDao?.update(cat)
    }

    private fun updateSQLDataBase(cat: Cats?) {
        val status = cat?.let { sqlOpenHelper?.updateCatFromSQL(it) }
        if (status != null) {
            if(status > -1) {
                Toast.makeText(activity?.applicationContext,"record update",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        binding?.idNameET?.text?.clear()
        binding?.idAgeET?.text?.clear()
        binding?.idBreedET?.text?.clear()
    }

    fun setInterface(inter: OnAddNewCatInDbListener) {
        this.addNewCatListener = inter
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