package ru.npc_ksb.alfaknd.domain_layer


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import ru.npc_ksb.alfaknd.presentation_layer.model.ChangedDatum
import ru.npc_ksb.alfaknd.data_layer.repository.AlfaKndRepository
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.AlfaKndViewModel
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.DiskDatumDataStore
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility as DataLayerTypesOfResponsibility

class InspectionsImpl(fragment: Fragment, contextForInspections: Context) : Inspections {

    val TAG: String = "myLog"

    val repository: AlfaKndRepository = ViewModelProviders.of(fragment).get(AlfaKndViewModel::class.java).repository

    //наблюдаемы    е втсавленные данные

//    var datum = Datum()
    var datumFromFileProvider = DiskDatumDataStore(repository)
    var context = contextForInspections.applicationContext
    override var dataForUpdate: Single<MutableList<DataLayerTypesOfResponsibility>>? = null
    override var dataForInsert: Single<MutableList<DataLayerTypesOfResponsibility>>? = null


    override fun sync() {
        datumFromFileProvider!!.sync()
        dataForInsert = insertDatums()
        dataForUpdate = updateDatums()
    }

    override fun updateDatums(): Single<MutableList<DataLayerTypesOfResponsibility>> {
        val changedDatums = datumFromFileProvider!!.changedDatums
        val dataForUpdate = getDataForUpdate(changedDatums).toList()
        return update(dataForUpdate!!)
    }

    override fun insertDatums(): Single<MutableList<DataLayerTypesOfResponsibility>> {
        val changedDatums = datumFromFileProvider!!.changedDatums
        val dataForInsert = getDataForInsert(changedDatums).toList()
        return insert(dataForInsert!!)
    }

    private fun getDataForInsert(changedDatums: MutableSet<ChangedDatum>?): MutableSet<DataLayerTypesOfResponsibility> {
        var datasForInsert = mutableSetOf<DataLayerTypesOfResponsibility>()
        for (changedDatum in changedDatums!!.iterator()) {
            //имитация установки локального состояния
            changedDatum.clickedLocalRadio()
            when {
                //если нажали установить локальные данные
                (changedDatum.localRadio) -> datasForInsert.add(changedDatum.localTypesOfResponsibility)
                //если нажали установить удаленные данные
//                (changedDatum.remoteRadio) -> datasForInsert.add(changedDatum.remoteTypesOfResponsibility)
            }
        }
        return datasForInsert
    }

    private fun getDataForUpdate(changedDatums: MutableSet<ChangedDatum>?): MutableSet<DataLayerTypesOfResponsibility> {
        var datasForUpdate = mutableSetOf<DataLayerTypesOfResponsibility>()
        for (dataForUpdate in changedDatums!!.iterator()) {
            //имитация установки локального состояния
            dataForUpdate.clickedLocalRadio()
            when {
                (dataForUpdate.localRadio) -> datasForUpdate.add(dataForUpdate.localTypesOfResponsibility)
//                (dataForUpdate.remoteRadio) -> datasForUpdate.add(dataForUpdate.remoteTypesOfResponsibility)
            }
        }
        return datasForUpdate
    }


    private fun update(dataForUpdate: List<DataLayerTypesOfResponsibility>): Single<MutableList<DataLayerTypesOfResponsibility>> {
        return Observable.fromCallable { repository.updateList(dataForUpdate) }
                .subscribeOn(Schedulers.io())
                //перебираем список
                .flatMapIterable {
                    //элемент списка приобразовываем в Long
                    it.map {
                        it.toInt()
                    }
                }
                //объеденить выпущенные элементы из map в список
                .toList()
                .map {
                    it.toMutableList()
                }
                .map {
                    it.map { it ->
                        repository.getById(it)
                    }
                }.map {
                    it.toMutableList()
                }.flatMap {
                    Single.just(it)
                }
    }

    private fun insert(datasForInsert: List<DataLayerTypesOfResponsibility>): Single<MutableList<DataLayerTypesOfResponsibility>> {
        return Observable.fromCallable { repository.insertList(datasForInsert) }
                .subscribeOn(Schedulers.io())
                //перебираем список
                .flatMapIterable {
                    //элемент списка приобразовываем в Long
                    it.map {
                        it.toInt()
                    }
                }
                //объеденить выпущенные элементы из map в список
                .toList()
                .map {
                    it.toMutableList()
                }
                .map {
                    it.map { it ->
                        repository.getById(it)
                    }
                }.map {
                    it.toMutableList()
                }.flatMap {
                    Single.just(it)
                }
    }
}