package ru.npc_ksb.alfaknd.data.preferences

import android.content.Context
import android.content.SharedPreferences


object FirstSyncData {
    private const val SYNC_PREFERENCE = "FirstSyncData"
    private const val IS_SYNCHRONIZED = "IS_SYNCHRONIZED"

    fun SyncPreference(context: Context): SharedPreferences = context.getSharedPreferences(SYNC_PREFERENCE, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var SharedPreferences.isSync
        get() = getBoolean(IS_SYNCHRONIZED, false)
        set(value) {
            editMe { editor ->
                editor.putBoolean(IS_SYNCHRONIZED, value)
            }
        }

    fun SharedPreferences.clearValues(){
        editMe {
                editor -> editor.clear()
        }
    }
}