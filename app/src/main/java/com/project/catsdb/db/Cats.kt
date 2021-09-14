package com.project.catsdb.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cats (
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var age: String? = null,
    var breed: String? = null
)