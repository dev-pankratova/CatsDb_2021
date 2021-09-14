package com.project.catsdb

import android.app.Application
import com.project.catsdb.db.AppDatabase

import androidx.room.Room

class App : Application() {

    private var database: AppDatabase? = null
    var instance: App? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDatabase::class.java, "cats").allowMainThreadQueries()
            .build()
    }

    @JvmName("getInstance1")
    fun getInstance(): App? {
        return instance
    }

    fun getDatabase(): AppDatabase? {
        return database
    }
}