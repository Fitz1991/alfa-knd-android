package ru.npc_ksb.alfaknd.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import io.reactivex.Single
import ru.npc_ksb.alfaknd.models.Datum
import ru.npc_ksb.alfaknd.models.DatumDao

class AlfaKndRepository(private val datumDao: DatumDao) {

    val allDatum: LiveData<List<Datum>> = datumDao.getAll()

    fun getById(id: Int) : Single<Datum> {
        return datumDao.getById(id)
    }

    @WorkerThread
    fun insert(datum: Datum) : Long {
        return datumDao.insert(datum)
    }

    @WorkerThread
    fun update(datum: Datum) : Int{
        return datumDao.update(datum)
    }

    @WorkerThread
    fun delete(datum: Datum) {
        datumDao.delete(datum)
    }
}