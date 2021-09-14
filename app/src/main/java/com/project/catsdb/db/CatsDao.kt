package com.project.catsdb.db

import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.Database

@Dao
interface CatsDao {

    @Insert
    fun insert(cat: Cats?)

    @Update
    fun update(cat: Cats?)

    @Query("DELETE FROM Cats")
    fun deleteAll()

/*    @Query("SELECT * FROM Cats WHERE id = :id")
    fun getById(id: Int): Cats?*/

    @Query("SELECT * FROM Cats")
    fun getAll(): List<Cats>?
}