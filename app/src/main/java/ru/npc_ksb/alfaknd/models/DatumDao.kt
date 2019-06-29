package ru.npc_ksb.alfaknd.models

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface DatumDao {

    @Query("SELECT * FROM datum")
    fun getAll():LiveData<List<Datum>>


    @Query("SELECT * FROM datum WHERE pk = :id")
    fun getById(id: Int): LiveData<Datum>


    @Insert
    fun insert(employee:Datum) : Flowable<Long>


    @Update
    fun update(employee: Datum) : Flowable<Int>

    @Delete
    fun delete(employee:Datum)

    @Query("DELETE FROM datum")
    fun deleteAll()
}