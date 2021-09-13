package com.project.catsdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.catsdb.databinding.ListOfCatsFragmentBinding

class MainListOfCats : Fragment() {

    private var binding: ListOfCatsFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListOfCatsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionFloatBtn()
        setClickClearBtn()
    }

    private fun setActionFloatBtn() {
        binding?.floatBtn?.setOnClickListener{ openAddNewFragment() }
    }
    // TODO openAddNewFragment
    private fun openAddNewFragment() {

    }

    private fun setClickClearBtn() {
        binding?.buttonClear?.setOnClickListener { clearDb() }
    }
    //TODO clearDb
    private fun clearDb() {

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