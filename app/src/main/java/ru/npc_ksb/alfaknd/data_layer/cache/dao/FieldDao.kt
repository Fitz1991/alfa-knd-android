package ru.npc_ksb.alfaknd.data_layer.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.npc_ksb.alfaknd.presentation_layer.model.Field

@Dao
interface FieldDao {
    @Query("SELECT * FROM field")
    fun getAll(): LiveData<List<Field>>

    @Query("SELECT label FROM field WHERE name = :name")
    fun getLabel(name : String): String
}