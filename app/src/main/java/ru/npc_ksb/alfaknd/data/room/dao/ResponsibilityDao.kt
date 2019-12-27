package ru.npc_ksb.alfaknd.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import ru.npc_ksb.alfaknd.data.room.entities.Responsibility


@Dao
interface ResponsibilityDao : CRUDDao<Responsibility> {

    @Query("SELECT * FROM responsibility")
    override fun getAll(): LiveData<List<Responsibility>>


    @Query("SELECT * FROM Responsibility WHERE uuid = :uuid")
    override fun getByUUID(uuid: String): Responsibility

    @Delete
    override fun delete(enity: Responsibility): Int

    @Delete
    override fun deleteList(enityList: List<Responsibility>)

    @Query("DELETE FROM responsibility")
    override fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(enity: Responsibility)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertList(enityList: List<Responsibility>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override fun updateList(enityList: List<Responsibility>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override fun update(enity: Responsibility)

    @Query("SELECT * FROM Responsibility ORDER BY uuid DESC LIMIT :count")
    fun getLimitedData(count: Int?): LiveData<List<Responsibility>>

    //    @Query("SELECT *  FROM Responsibility WHERE :field Like :value ORDER BY Responsibility.uuid")
    @RawQuery(observedEntities = [Responsibility::class])
    fun getFilteredData(query: SupportSQLiteQuery): LiveData<List<Responsibility>>

    @Query("SELECT count(*) FROM Responsibility")
    fun getAllDataCount(): LiveData<Int>

    @RawQuery(observedEntities = arrayOf(Responsibility::class))
    fun getAllDataCount(query: SimpleSQLiteQuery): LiveData<Int>

    @Query("SELECT * FROM Responsibility LIMIT :limit OFFSET :offset")
    fun getPage(offset: Int, limit: Int): LiveData<List<Responsibility>>
}