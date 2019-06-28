package ru.npc_ksb.alfaknd.Repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ru.npc_ksb.alfaknd.models.Datum
import ru.npc_ksb.alfaknd.models.DatumDao

class AlfaKndRepository(private val datumDao: DatumDao) {

    val allDatum: LiveData<List<Datum>> = datumDao.getAll()


    fun getById(id: Long) : Single<Datum> {
        return datumDao.getById(id)
        Log.d("myLog", "AlfaKndRepository")
    }

    @WorkerThread
    fun insert(datum: Datum) : Long{
        return datumDao.insert(datum)
    }

    @WorkerThread
    fun update(datum: Datum) : Int {
        return datumDao.update(datum)
    }

    @WorkerThread
    fun delete(datum: Datum) {
        datumDao.delete(datum)
    }
}