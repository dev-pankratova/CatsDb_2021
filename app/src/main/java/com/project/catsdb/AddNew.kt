package com.project.catsdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.catsdb.databinding.AddNewFragmentBinding

class AddNew : Fragment() {
    private var binding: AddNewFragmentBinding? = null

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
        binding?.addButton?.setOnClickListener { addToDB() }
    }

    private fun addToDB() {
        val name = binding?.idNameET?.text.toString()
        val age = binding?.idAgeET?.text.toString()
        val breed = binding?.idBreedET?.text.toString()

        // TODO add to DB and return to MainListOfCats
        val dao = TableDao()
        dao.add(Table(null, name, age.toInt(), breed))

        /*val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()*/
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): AddNew {
            val fragment = AddNew()

            return fragment
        }
    }
}