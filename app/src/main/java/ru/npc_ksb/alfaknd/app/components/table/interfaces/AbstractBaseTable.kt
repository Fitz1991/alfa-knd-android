package ru.npc_ksb.alfaknd.app.components.table.interfaces

import android.os.Parcelable
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.app.components.table.column.Column

abstract class AbstractBaseTable : Parcelable {
    var pageSizeOptions: Array<String> =
        MyApplication.resource.getStringArray(R.array.pageSizeOptions)
    var columnData: MutableMap<Int, MutableList<String?>>? = null
    var clickableColumns: List<Int>? = null
    abstract var columns: MutableMap<Int, Column>?
    abstract var repositoryClass: Class<*>
    var searchFilter: Boolean = false

    fun columns(columns: MutableMap<Int, Column>) = apply { this.columns = columns }
    fun searchFilter(searchFilter: Boolean) = apply { this.searchFilter = searchFilter }
    fun clickableColumns(clickableColumns: List<Int>) =
        apply { this.clickableColumns = clickableColumns }

    fun repositoryClass(repositoryClass: Class<*>) =
        apply { this.repositoryClass = repositoryClass }

    fun build() = kotlin.run { this }
}