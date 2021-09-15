package com.project.catsdb.listeners

import com.project.catsdb.db.Cats

interface OnAdapterClick {

    fun getCatsData(cat: Cats)
}