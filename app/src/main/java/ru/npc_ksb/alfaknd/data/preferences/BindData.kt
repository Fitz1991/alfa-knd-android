package ru.npc_ksb.alfaknd.data.preferences

import android.content.Context
import android.content.SharedPreferences


object BindData {
    private const val BIND_PREFERENCE = "BindData"
    private const val USER_HOST = "HOST"
    private const val USER_TOKEN = "TOKEN"

    fun BindPreference(context: Context): SharedPreferences = context.getSharedPreferences(BIND_PREFERENCE, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.host
        get() = getString(USER_HOST, "")!!
        set(value) {
            editMe {
                it.putString(USER_HOST, value)
            }
        }

    var SharedPreferences.token
        get() = getString(USER_TOKEN, "")!!
        set(value) {
            editMe {
                it.putString(USER_TOKEN, value)
            }
        }

    fun SharedPreferences.clearValues(){
        editMe {
            it.clear()
        }
    }
}
