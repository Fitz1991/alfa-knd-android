package ru.npc_ksb.alfaknd.domain.service

import androidx.lifecycle.LiveData
import ru.npc_ksb.alfaknd.data.models.MainModel

interface InspectionVideoService{
    var viewModel: MainModel

    fun saveInspectionVideo(inspectionVideo: InspectionVideo)

    fun getInspectionVideos(inspectionUUID: String): LiveData<List<InspectionVideo>>

    fun deleteInspectionVideo(inspectionVideo: InspectionVideo)
}