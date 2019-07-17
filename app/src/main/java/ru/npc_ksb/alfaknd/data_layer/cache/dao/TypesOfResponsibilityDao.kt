package ru.npc_ksb.alfaknd.data_layer.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility

@Dao
interface TypesOfResponsibilityDao {

    @Query("SELECT * FROM typesofresponsibility")
    fun getAll():LiveData<List<TypesOfResponsibility>>

    @Query("SELECT * FROM typesofresponsibility WHERE uuid IN (:changedDatums)")
           fun getOldDatum(changedDatums: List<Long>) : List<TypesOfResponsibility>

    @Query("SELECT * FROM typesofresponsibility WHERE uuid = :id")
    fun getById(id: Int): TypesOfResponsibility


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employee: TypesOfResponsibility) : Long


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(employee: TypesOfResponsibility) : Int

    @Delete
    fun delete(employee: TypesOfResponsibility)

    @Query("DELETE FROM typesofresponsibility")
    fun deleteAll()

    @Insert
//    (onConflict = OnConflictStrategy.REPLACE)
    fun insertList(datumsForInsert: List<TypesOfResponsibility>): List<Long>

    @Insert
    (onConflict = OnConflictStrategy.REPLACE)
    fun updateList(datumsForUpdate: List<TypesOfResponsibility>) : List<Long>

}