package ru.npc_ksb.alfaknd.models

import android.content.ContentValues
import android.util.Log

class Test {
    companion object {
        const val TABLE = "test_table"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"

        fun addName(name: String) {
            val db = DB.Write()
            val values = ContentValues()
            values.put(COLUMN_NAME, name)
            db.insert(TABLE, null, values)
            db.close()
        }

        fun getNames() {
            val db = DB.Read()
            val cursor = db.rawQuery("SELECT $COLUMN_ID, $COLUMN_NAME FROM $TABLE", null)
            cursor!!.moveToFirst()
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex((COLUMN_ID)))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                Log.d("qwerty", "id: $id, name: $name")
            }
            cursor.close()
            db.close()
        }
    }
}
