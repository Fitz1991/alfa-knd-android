package ru.npc_ksb.alfaknd.presentation_layer.view.fragments

import io.reactivex.Single
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility

interface InspectionsFView {
    fun showInsertedData(insertedDatas: Single<MutableList<TypesOfResponsibility>>?)
    fun showUpdatedData(updatedDatas: Single<MutableList<TypesOfResponsibility>>?)
}