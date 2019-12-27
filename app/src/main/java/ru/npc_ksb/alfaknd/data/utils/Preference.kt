package ru.npc_ksb.alfaknd.data.utils

import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.data.preferences.*
import timber.log.Timber
import ru.npc_ksb.alfaknd.data.preferences.AuthoritySyncData.clearValues as AuthorityClearValues
import ru.npc_ksb.alfaknd.data.preferences.BindData.clearValues as BindDataClearValues
import ru.npc_ksb.alfaknd.data.preferences.FirstSyncData.clearValues as FirstSyncClearValues
import ru.npc_ksb.alfaknd.data.preferences.FirstSyncData.isSync as FirstSync
import ru.npc_ksb.alfaknd.data.preferences.InspectionsSyncData.clearValues as InspectionsClearValues
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.clearValues as ProfileDataClearValues


fun cleanSyncPreferenceData(){
    val firstSyncSyncData = FirstSyncData.SyncPreference(MyApplication.context)
    val authoritySyncData = AuthoritySyncData.SyncPreference(MyApplication.context)
    val inspectionSyncData = InspectionsSyncData.SyncPreference(MyApplication.context)

    firstSyncSyncData.FirstSyncClearValues()
    authoritySyncData.AuthorityClearValues()
    inspectionSyncData.InspectionsClearValues()
}

fun cleanProfileData(){
    val userData = ProfileData.ProfilePreference(MyApplication.context)
    val bindData = BindData.BindPreference(MyApplication.context)

    userData.ProfileDataClearValues()
    bindData.BindDataClearValues()
    cleanSyncPreferenceData()
}

fun checkFirstSyncPreferenceData(): Boolean{
    val firstSyncSyncData = FirstSyncData.SyncPreference(MyApplication.context)
    Timber.d("--------------  : ${firstSyncSyncData.FirstSync}")
    return firstSyncSyncData.FirstSync
}