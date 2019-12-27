package ru.npc_ksb.alfaknd.data.models

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import ru.npc_ksb.alfaknd.data.repository.*
import ru.npc_ksb.alfaknd.data.room.AppDatabase

@Parcelize
class MainModel(@get:JvmName("application") val application: @RawValue Application) :
    AndroidViewModel(application), Parcelable {
    @IgnoredOnParcel
    val TAG: String = "myLog"
    @IgnoredOnParcel
    val responsibilityRepository: ResponsibilityRepository

    @IgnoredOnParcel
    val db: RoomDatabase

    init {
        db = AppDatabase.getDatabase(application, viewModelScope)
        val responsibilityDao = db.responsibilityDao()

        responsibilityRepository = ResponsibilityRepository(responsibilityDao)

        syncAuthorityRepository = SyncAuthorityRepository(
            responsibilityDao
        )

        syncInspectionRepository = SyncInspectionRepository(
            inspectionsDao,
            inspectionDocumentDao,
            inspectionChecklistDao,
            inspectionAnswerChecklistDao
        )
    }

    fun getRepository(repositoryClass: Class<*>): Any? {
        when (repositoryClass) {
            ResponsibilityRepository::class.java -> return responsibilityRepository
            InspectionsRepository::class.java -> return inspectionsRepository
            InspectionChecklistRepository::class.java -> return inspectionChecklistRepository
            InspectionAnswerChecklistRepository::class.java -> return inspectionAnswerChecklistRepository
            InspectionDocumentRepository::class.java -> return inspectionDocumentRepository
        }
        return null
    }
}