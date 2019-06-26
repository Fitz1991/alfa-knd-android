package ru.npc_ksb.alfaknd

import android.app.Application






class Application: Application() {

    override fun onCreate() {
        Session.startWorker()
        super.onCreate()
    }
}
