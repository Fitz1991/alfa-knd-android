package ru.npc_ksb.alfaknd.domain.service

import androidx.lifecycle.LiveData
import ru.npc_ksb.alfaknd.data.models.MainModel

interface InspectionImageService{
    var viewModel: MainModel

    fun saveInspectionImage(inspectionImage: InspectionImage)

    fun getInspectionImages(inspectionUUID: String): LiveData<List<InspectionImage>>

    fun deleteInspectionImage(inspectionImage: InspectionImage)

}