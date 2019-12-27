package ru.npc_ksb.alfaknd.data.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.npc_ksb.alfaknd.app.components.table.filter.FilterItem
import ru.npc_ksb.alfaknd.app.components.table.interfaces.TableRepository
import ru.npc_ksb.alfaknd.data.room.dao.ResponsibilityDao
import ru.npc_ksb.alfaknd.data.room.entities.Responsibility


class ResponsibilityRepository(private val responsibilityDao: ResponsibilityDao) :
    CRUDRepository<Responsibility, ResponsibilityDao>(responsibilityDao),
    TableRepository<Responsibility> {

    override val ALL: String = "*"

    fun getLimitedData(count: Int?): LiveData<List<Responsibility>> {
        if (count == 0) {
            return responsibilityDao.getAll()
        } else {
            return responsibilityDao.getLimitedData(count)
        }
    }

    override fun getAllDataCount(enity: BaseEnity?): LiveData<Int> {
        return responsibilityDao.getAllDataCount()
    }

    private fun buildQuery(
        filters: HashSet<FilterItem>?,
        searchText: String?,
        selection: String?
    ): String {
        var query: String
        if (filters != null && filters.isNotEmpty()) {
            query = "SELECT $selection FROM Responsibility WHERE "
            filters.forEach {
                if (it != filters.last()) {
                    query += "UPPER(${it.field}) LIKE UPPER('${it.value}') AND "
                } else {
                    query += "UPPER(${it.field}) LIKE UPPER('${it.value}')"
                }
            }
        } else if (searchText != null) {
            query =
                "SELECT $selection FROM Responsibility WHERE UPPER(statement) LIKE UPPER('%${searchText}%') " +
                        "OR UPPER(npa) LIKE UPPER('%${searchText}%') " +
                        "OR UPPER(structuralUnits) LIKE UPPER('%${searchText}%') " +
                        "OR UPPER(punishments) LIKE UPPER('%${searchText}%')"
        } else {
            query = "SELECT $selection FROM Responsibility"
        }
        return query
    }

    override fun getPage(
        offset: Int,
        limit: Int,
        filters: HashSet<FilterItem>?,
        searchText: String?
    ): LiveData<List<Responsibility>> {
        var queryBuilder = buildQuery(filters, searchText, ALL)
        queryBuilder += " ORDER BY Responsibility.uuid LIMIT $limit OFFSET $offset "
        val query = SimpleSQLiteQuery(queryBuilder)
        return responsibilityDao.getFilteredData(query)
    }

    override fun getPageCount(filters: HashSet<FilterItem>?, searchText: String?): LiveData<Int> {
        var queryBuilder = buildQuery(filters, searchText, COUNT)
        val query = SimpleSQLiteQuery(queryBuilder)
        return responsibilityDao.getAllDataCount(query)
    }

    override fun toString(): String {
        return "Виды ответственности"
    }
}