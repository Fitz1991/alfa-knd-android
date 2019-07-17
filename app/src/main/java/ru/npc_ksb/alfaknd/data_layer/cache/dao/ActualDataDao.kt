package ru.npc_ksb.alfaknd.data_layer.cache.dao

import androidx.room.*
import ru.npc_ksb.alfaknd.presentation_layer.model.ActualData

@Dao
interface ActualDataDao {

    @Query("SELECT * FROM actualdata")
    fun getLastUpdatedData(): ActualData

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLastUpdatedData(actualData: ActualData)
}