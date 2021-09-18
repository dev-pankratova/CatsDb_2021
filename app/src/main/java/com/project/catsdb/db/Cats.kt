package com.project.catsdb.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Cats (
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var age: Int? = null,
    var breed: String? = null
): Serializable