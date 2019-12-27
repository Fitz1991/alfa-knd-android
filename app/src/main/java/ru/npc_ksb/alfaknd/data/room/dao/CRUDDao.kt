package ru.npc_ksb.alfaknd.data.room.dao

import androidx.lifecycle.LiveData

interface CRUDDao<T : BaseEnity> {
    fun getByUUID(uuid: String): T
    fun delete(enity: T): Int
    fun deleteList(enityList: List<@JvmSuppressWildcards T>)
    fun deleteAll()
    fun insert(enity: T)
    fun insertList(enityList: List<@JvmSuppressWildcards T>)
    fun updateList(enityList: List<@JvmSuppressWildcards T>)
    fun update(enity: T)
    fun getAll():LiveData<List<T>>
}