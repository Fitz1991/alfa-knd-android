package ru.npc_ksb.alfaknd.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB(context: Context, factory: SQLiteDatabase.CursorFactory? = null) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "knd.db"

        var Instance: DB? = null

        fun init(context: Context) {
            if (Instance == null) {
                Instance = DB(context)
            }
        }

        fun Read(): SQLiteDatabase {
            return Instance!!.readableDatabase
        }

        fun Write(): SQLiteDatabase {
            return Instance!!.writableDatabase
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TEST_TABLE = ("""
            CREATE TABLE test_table (
              id   INTEGER PRIMARY KEY,
              name TEXT
            )
        """)
        db.execSQL(CREATE_TEST_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}
