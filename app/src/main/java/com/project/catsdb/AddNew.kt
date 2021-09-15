package com.project.catsdb

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.project.catsdb.databinding.AddNewFragmentBinding
import com.project.catsdb.db.AppDatabase
import com.project.catsdb.db.Cats
import com.project.catsdb.db.CatsDao
import com.project.catsdb.listeners.OnAddNewCatInDbListener

class AddNew : Fragment() {
    private var binding: AddNewFragmentBinding? = null
    private var db: AppDatabase? = null
    private var catsDao: CatsDao? = null
    private var addNewCatListener: OnAddNewCatInDbListener? =null

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

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        db = (activity?.application as App).getInstance()?.getDatabase()
        catsDao = db?.catsDao()

        binding?.addButton?.setOnClickListener { addToDB() }

        val item = arguments?.getSerializable("CatsItem")

        if (item != null) {
            fillDataForUpdate(item as Cats)
            renameButton()
            setUpdateClick(item)
        }
    }

    override fun onResume() {
        super.onResume()
        clearEditTextFields()
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
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun fillDataForUpdate(item: Cats) {
        binding?.idNameET?.setText(item.name)
        binding?.idAgeET?.setText(item.age)
        binding?.idBreedET?.setText(item.breed)
    }

    private fun renameButton() {
        binding?.addButton?.text = "Update"
    }

    private fun setUpdateClick(item: Cats) {
        binding?.addButton?.setOnClickListener {
            catsDao?.update(item)
            addNewCatListener?.addInDb(true)
        }
    }

    private fun clearEditTextFields() {
        binding?.idNameET?.text?.clear()
        binding?.idAgeET?.text?.clear()
        binding?.idBreedET?.text?.clear()
    }

    private fun addToDB() {
        val name = binding?.idNameET?.text.toString()
        val age = binding?.idAgeET?.text.toString()
        val breed = binding?.idBreedET?.text.toString()

        val newCat = Cats()

        newCat.name = name
        newCat.age = age
        newCat.breed = breed

        catsDao?.insert(newCat)
        addNewCatListener?.addInDb(true)
    }

    fun setInterface(inter: OnAddNewCatInDbListener) {
        this.addNewCatListener = inter
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(model: Cats? = null): AddNew {
            val fragment = AddNew()
            val args = Bundle()
            args.putSerializable("CatsItem", model)
            return fragment
        }
    }
}