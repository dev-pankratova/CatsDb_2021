package com.project.catsdb.listeners

import com.project.catsdb.db.Cats

interface OnSendClickDataToActivity {
    fun sendData(cat: Cats) {
    }
}