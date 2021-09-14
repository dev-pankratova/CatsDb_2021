package com.project.catsdb.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.preference.PreferenceFragmentCompat
import com.project.catsdb.MainActivity
import com.project.catsdb.R

class SortingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        return
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)
        addPreferencesFromResource(R.xml.pref1)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.item1).isVisible = false
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(): SortingFragment {
            val fragment = SortingFragment()

            return fragment
        }
    }
}