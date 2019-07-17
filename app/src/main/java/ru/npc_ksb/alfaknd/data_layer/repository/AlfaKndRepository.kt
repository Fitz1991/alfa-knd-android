package ru.npc_ksb.alfaknd.data_layer.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ru.npc_ksb.alfaknd.data_layer.cache.dao.ActualDataDao
import ru.npc_ksb.alfaknd.data_layer.cache.dao.TypesOfResponsibilityDao
import ru.npc_ksb.alfaknd.data_layer.cache.dao.FieldDao
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility
import ru.npc_ksb.alfaknd.presentation_layer.model.ActualData

class AlfaKndRepository(private val datumDao: TypesOfResponsibilityDao,
                        private val actualDataDao: ActualDataDao,
                        private val fieldDao: FieldDao
                        ) {

    val allTypesOfResponsibility: LiveData<List<TypesOfResponsibility>> = datumDao.getAll()

    fun getById(id: Int) : TypesOfResponsibility {
        return datumDao.getById(id)
    }

    fun getLastUpdatedData() : ActualData {
        return actualDataDao.getLastUpdatedData()
    }

    fun getOldDatum(changedDatums : List<Long>) : List<TypesOfResponsibility> {
        return datumDao.getOldDatum(changedDatums)
    }

    fun getLabel(name: String) : String {
        return fieldDao.getLabel(name)
    }

    fun updateLastUpdatedData(actualData: ActualData) {
        return actualDataDao.updateLastUpdatedData(actualData)
    }

    @WorkerThread
    fun insert(typesOfResponsibility: TypesOfResponsibility) : Long {
        return datumDao.insert(typesOfResponsibility)
    }

    @WorkerThread
    fun insertList(dataForInsert: List<TypesOfResponsibility>) : List<Long>{
        return datumDao.insertList(dataForInsert)
    }

    @WorkerThread
    fun updateList(dataForUpdate: List<TypesOfResponsibility>) : List<Long>{
        return datumDao.updateList(dataForUpdate)
    }

    @WorkerThread
    fun update(typesOfResponsibility: TypesOfResponsibility) : Int{
        return datumDao.update(typesOfResponsibility)
    }



    @WorkerThread
    fun delete(typesOfResponsibility: TypesOfResponsibility) {
        datumDao.delete(typesOfResponsibility)
    }

}