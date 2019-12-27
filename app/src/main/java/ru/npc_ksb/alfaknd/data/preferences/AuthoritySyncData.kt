package ru.npc_ksb.alfaknd.data.preferences

import android.content.Context
import android.content.SharedPreferences

object AuthoritySyncData {
    private const val SYNC_PREFERENCE = "AuthoritySyncData"
    private const val LAST_UPDATE_DATE_TIME = "LASTUPDATEDATETIME"

    fun SyncPreference(context: Context): SharedPreferences = context.getSharedPreferences(SYNC_PREFERENCE, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var SharedPreferences.lastUpdateDateTime
        get() = getString(LAST_UPDATE_DATE_TIME, "")!!
        set(value) {
            editMe {editor ->
                editor.putString(LAST_UPDATE_DATE_TIME, value)
            }
        }

    fun SharedPreferences.clearValues(){
        editMe {
                editor -> editor.clear()
        }
    }
}