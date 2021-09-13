package com.project.catsdb.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.project.catsdb.R

class SortingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        return
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addPreferencesFromResource(R.xml.pref1)
    }

    companion object {
        fun newInstance(): SortingFragment {
            val fragment = SortingFragment()

            return fragment
        }
    }
}