package com.project.catsdb.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

internal const val TOPIC_COLUMN_ID = "ID"
internal const val TOPIC_COLUMN_NAME = "NAME"
internal const val TOPIC_COLUMN_AGE = "AGE"
internal const val TOPIC_COLUMN_BREED = "BREED"

private const val LOG_TAG = "SQL_LOGS"
private const val DATABASE_NAME = "SQL_CATS"
private const val TABLE_NAME = "cats_table"
private const val DATABASE_VERSION = 1
private const val CREATE_TABLE_SQL =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($TOPIC_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $TOPIC_COLUMN_NAME VARCHAR(50), $TOPIC_COLUMN_AGE VARCHAR(50), $TOPIC_COLUMN_BREED VARCHAR(50));"

class SQLiteOpenHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(CREATE_TABLE_SQL)

        } catch (exception: SQLException) {
            Log.e(LOG_TAG, "Exception while trying to create database", exception)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "onUpgrade called")
    }

    private fun getCursorWithTopics(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getListOfTopics(): List<Cats> {
        val listOfTopics = mutableListOf<Cats>()
        getCursorWithTopics().use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val topicId = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_ID))
                    val topicName = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_NAME))
                    val topicAge = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_AGE))
                    val topicBreed = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_BREED))
                    val catsList = Cats(topicId.toInt(), topicName, topicAge.toInt(), topicBreed)
                    listOfTopics.add(catsList)
                } while (cursor.moveToNext())
            }
        }
        return listOfTopics
    }

    fun saveRecord(cat: Cats): String {
        if(cat.name?.trim() != "" && cat.age.toString().trim() != "" && cat.age.toString().trim() != "null" && cat.breed?.trim() != "") {
            val status = this.addNewCat(cat)
            if(status > -1) {
                return "success"
            }
            return "notBlank"
        } else {
            return "blank"
        }
    }

    private fun addNewCat(cat: Cats): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TOPIC_COLUMN_NAME, cat.name)
        contentValues.put(TOPIC_COLUMN_AGE,cat.age )
        contentValues.put(TOPIC_COLUMN_BREED,cat.breed )

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    fun updateCatFromSQL(cat: Cats): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TOPIC_COLUMN_NAME, cat.name)
        contentValues.put(TOPIC_COLUMN_AGE,cat.age )
        contentValues.put(TOPIC_COLUMN_BREED,cat.breed )

        val success = db.update(TABLE_NAME, contentValues, "id=" + cat.id, null)
        db.close()
        return success
    }

    private fun getCursorWithTopicsSortByName(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $TOPIC_COLUMN_NAME ASC", null)
    }

    private fun getCursorWithTopicsSortByAge(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $TOPIC_COLUMN_AGE ASC", null)
    }

    private fun getCursorWithTopicsSortByBreed(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $TOPIC_COLUMN_BREED ASC", null)
    }

    fun getSortByName(): List<Cats> {
        val listOfTopics = mutableListOf<Cats>()
        getCursorWithTopicsSortByName().use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val topicId = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_ID))
                    val topicName = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_NAME))
                    val topicAge = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_AGE))
                    val topicBreed = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_BREED))
                    val catsList = Cats(topicId.toInt(), topicName, topicAge.toInt(), topicBreed)
                    listOfTopics.add(catsList)
                } while (cursor.moveToNext())
            }
        }
        return listOfTopics
    }

    fun getSortByAge(): List<Cats> {
        val listOfTopics = mutableListOf<Cats>()
        getCursorWithTopicsSortByAge().use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val topicId = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_ID))
                    val topicName = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_NAME))
                    val topicAge = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_AGE))
                    val topicBreed = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_BREED))
                    val catsList = Cats(topicId.toInt(), topicName, topicAge.toInt(), topicBreed)
                    listOfTopics.add(catsList)
                } while (cursor.moveToNext())
            }
        }
        return listOfTopics
    }

    fun getSortByBreed(): List<Cats> {
        val listOfTopics = mutableListOf<Cats>()
        getCursorWithTopicsSortByBreed().use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val topicId = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_ID))
                    val topicName = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_NAME))
                    val topicAge = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_AGE))
                    val topicBreed = cursor.getString(cursor.getColumnIndex(TOPIC_COLUMN_BREED))
                    val catsList = Cats(topicId.toInt(), topicName, topicAge.toInt(), topicBreed)
                    listOfTopics.add(catsList)
                } while (cursor.moveToNext())
            }
        }
        return listOfTopics
    }

    fun clearDataBase() {
        val db = this.writableDatabase
        db.apply {
            delete(TABLE_NAME, null, null)
            close()
        }
    }
}