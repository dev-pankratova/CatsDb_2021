package com.project.catsdb.db

import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.Database

@Dao
interface CatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cat: Cats?)

    @Update
    fun update(cat: Cats?)

    @Query("DELETE FROM Cats")
    fun deleteAll()

    @Query("SELECT * FROM Cats")
    fun getAll(): List<Cats>?

    @Query("SELECT * FROM Cats ORDER by name ASC")
    fun getSortByName(): List<Cats>?

    @Query("SELECT * FROM Cats ORDER by age ASC")
    fun getSortByAge(): List<Cats>?

    @Query("SELECT * FROM Cats ORDER by breed ASC")
    fun getSortByBreed(): List<Cats>?
}