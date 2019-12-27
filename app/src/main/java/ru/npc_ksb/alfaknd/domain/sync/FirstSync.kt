package ru.npc_ksb.alfaknd.domain.sync

import android.annotation.SuppressLint
import android.content.SharedPreferences
import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.data.preferences.FirstSyncData
import ru.npc_ksb.alfaknd.data.preferences.FirstSyncData.isSync
import ru.npc_ksb.alfaknd.domain.sync.interfaces.BasicSyncCompleted

class FirstSync(activityContext: BasicSyncCompleted, viewModel: MainModel) : BasicSyncCompleted {

    val inspectionSync = InspectionSync(viewModel)
    val authoritySync = AuthoritySync(viewModel)
    val firstSyncData : SharedPreferences = FirstSyncData.SyncPreference(MyApplication.context)
    val activityContext = activityContext

    @SuppressLint("CheckResult")
    fun synchronize() {
        authoritySync.synchronize().doOnComplete {
            inspectionSync.synchronize()
                .doOnComplete{
                    onTaskCompleted()
                }
                .subscribe {}
        }.subscribe {}
    }

    override fun onTaskCompleted() {
        firstSyncData.isSync = true
        activityContext.onTaskCompleted()
    }
}