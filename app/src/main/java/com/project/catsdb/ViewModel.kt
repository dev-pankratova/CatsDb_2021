package com.project.catsdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatsViewModel : ViewModel() {
    val _modeDb = MutableLiveData<String>()
    val modeDb: LiveData<String> = _modeDb


    companion object {
        const val MODE_ROOM = "mode_room"
        const val MODE_CURSOR = "mode_cursor"
    }
}