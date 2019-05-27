package ru.npc_ksb.alfaknd

import android.app.Application
import android.content.Context
import ru.npc_ksb.alfaknd.db.Database

class Application: Application() {
    companion object {
        private var appContext: Context? = null

        fun getContext() = appContext!!
    }

    override fun onCreate() {
        appContext = applicationContext

        val db = Database(appContext!!)

        super.onCreate()
    }
}
