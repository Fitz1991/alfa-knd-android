package ru.npc_ksb.alfaknd.models

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DatumDao {

    @Query("SELECT * FROM datum")
    fun getAll():LiveData<List<Datum>>

    @Query("SELECT * FROM datum WHERE pk = :id")
    fun getById(id: Long): Single<Datum>

    @Insert
    fun insert(employee:Datum)

    @Update
    fun update(employee: Datum)

    @Delete
    fun delete(employee:Datum)

    @Query("DELETE FROM datum")
    fun deleteAll()
}