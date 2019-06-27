package ru.npc_ksb.alfaknd.models

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface DatumDao {

    @Query("SELECT * FROM datum")
    fun getAll():LiveData<List<Datum>>

    @Query("SELECT * FROM datum WHERE pk = :id")
    fun getById(id: Long): Single<Datum>


    @Insert
    fun insert(employee:Datum) : Single<Int>


    @Update
    fun update(employee: Datum) : Single<Int>

    @Delete
    fun delete(employee:Datum)

    @Query("DELETE FROM datum")
    fun deleteAll()
}