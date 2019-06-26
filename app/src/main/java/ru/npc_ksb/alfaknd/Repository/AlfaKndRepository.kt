package ru.npc_ksb.alfaknd.Repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ru.npc_ksb.alfaknd.models.Datum
import ru.npc_ksb.alfaknd.models.DatumDao

class AlfaKndRepository(private val datumDao: DatumDao) {

    val allDatum: LiveData<List<Datum>> = datumDao.getAll()


    fun getById(id: Long) : LiveData<Datum>{
        return datumDao.getById(id)
        Log.d("myLog", "AlfaKndRepository")
    }

    @WorkerThread
    suspend fun insert(datum: Datum) {
        datumDao.insert(datum)
    }

    @WorkerThread
    suspend fun update(datum: Datum) {
        datumDao.update(datum)
    }

    @WorkerThread
    suspend fun delete(datum: Datum) {
        datumDao.delete(datum)
    }
}