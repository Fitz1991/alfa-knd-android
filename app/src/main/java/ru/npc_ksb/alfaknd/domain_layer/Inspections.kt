package ru.npc_ksb.alfaknd.domain_layer

import io.reactivex.Single
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility

interface Inspections {
//    fun syncDatum(remoteDatums: List<Datum>, localDatums: List<Datum>): MutableMap<String, Observable<MutableList<Datum>>>
    fun updateDatums(): Single<MutableList<TypesOfResponsibility>>

    fun insertDatums(): Single<MutableList<TypesOfResponsibility>>

    fun sync()

    var dataForInsert : Single<MutableList<TypesOfResponsibility>>?
    var dataForUpdate : Single<MutableList<TypesOfResponsibility>>?
}