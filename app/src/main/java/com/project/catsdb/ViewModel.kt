package com.project.catsdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatsViewModel : ViewModel() {

    val modeDb1: LiveData<String> = _modeDb1

    fun setMode(mode: String) {
        _modeDb1.postValue(mode)
    }

    fun setModeDb(mode: String) {
        modeDb = mode
    }

    fun getModeDb(): String {
        return  modeDb
    }

    companion object {
        const val MODE_ROOM = "mode_room"
        const val MODE_CURSOR = "mode_cursor"
        var modeDb: String = MODE_ROOM
        val _modeDb1 = MutableLiveData<String>()
    }
}