package com.project.catsdb.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Cats::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catsDao(): CatsDao?
}