package ru.npc_ksb.alfaknd.app.components.table.interfaces

import androidx.lifecycle.LiveData
import ru.npc_ksb.alfaknd.app.components.table.filter.FilterItem


interface TableRepository<T : BaseEnity>{
    fun getAllDataCount(enity: BaseEnity?=null): LiveData<Int>
    fun getPage(
        offset: Int,
        limit: Int,
        filters: HashSet<FilterItem>? = null,
        searchText: String? = null
    ): LiveData<List<T>>

    fun getPageCount(
        filters: HashSet<FilterItem>?,
        searchText: String?
    ): LiveData<Int>
}