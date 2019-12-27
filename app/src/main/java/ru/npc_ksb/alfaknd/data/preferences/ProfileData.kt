package ru.npc_ksb.alfaknd.data.preferences

import android.content.Context
import android.content.SharedPreferences


object ProfileData {
    private const val PROFILE_PREFERENCE = "UserData"
    private const val USER_AUTH_PINCODE = "PINCODE"
    private const val USER_EMAIL = "EMAIL"
    private const val USER_USERNAME = "USERNAME"
    private const val USER_GUID = "USER_GUID"
    private const val AUTHORITY_NAME = "AUTHORITY_NAME"
    private const val AUTHORITY_GUID = "AUTHORITY_GUID"
    private const val DATA_TIME_BLOCKING = "DATA_TIME_BLOCKING"

    fun ProfilePreference(context: Context): SharedPreferences = context.getSharedPreferences(PROFILE_PREFERENCE, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.pincode
        get() = getString(USER_AUTH_PINCODE, "")!!
        set(value) {
            editMe {
                it.putString(USER_AUTH_PINCODE, value)
            }
        }

    var SharedPreferences.email
        get() = getString(USER_EMAIL, "")!!
        set(value) {
            editMe {
                it.putString(USER_EMAIL, value)
            }
        }

    var SharedPreferences.username
        get() = getString(USER_USERNAME, "")!!
        set(value) {
            editMe {
                it.putString(USER_USERNAME, value)
            }
        }

    var SharedPreferences.userGuid
        get() = getString(USER_GUID, "")!!
        set(value) {
            editMe {
                it.putString(USER_GUID, value)
            }
        }

    var SharedPreferences.authority
        get() = getString(AUTHORITY_NAME, "")!!
        set(value) {
            editMe {
                it.putString(AUTHORITY_NAME, value)
            }
        }

    var SharedPreferences.authorityGuid
        get() = getString(AUTHORITY_GUID, "")!!
        set(value) {
            editMe {
                it.putString(AUTHORITY_GUID, value)
            }
        }

    var SharedPreferences.dataTimeBlocking
        get() = getString(DATA_TIME_BLOCKING, "")!!
        set(value) {
            editMe {
                it.putString(DATA_TIME_BLOCKING, value)
            }
        }

    fun SharedPreferences.clearValues(){
        editMe {
            it.clear()
        }
    }
}
