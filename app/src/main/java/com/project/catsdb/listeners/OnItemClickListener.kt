package com.project.catsdb.listeners

import com.project.catsdb.db.Cats

interface OnItemClickListener {
    fun onVariantClick(model: Cats?)
}