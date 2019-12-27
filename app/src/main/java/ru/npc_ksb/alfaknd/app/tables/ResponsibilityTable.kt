package ru.npc_ksb.alfaknd.app.tables

import android.annotation.SuppressLint
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.components.table.column.Column
import ru.npc_ksb.alfaknd.app.components.table.interfaces.AbstractBaseTable
import ru.npc_ksb.alfaknd.data.repository.ResponsibilityRepository

@SuppressLint("ParcelCreator")
@Parcelize
class ResponsibilityTable : AbstractBaseTable() {
    @IgnoredOnParcel
    override var columns: MutableMap<Int, Column>? = mutableMapOf(
        R.id.statement to Column.Builder()
            .title("Формулировка нарушения")
            .weightColumn(2.6f)
            .field("statement")
            .build()
        ,
        R.id.npa to Column.Builder()
            .title("НПА")
            .weightColumn(0.4f)
            .field("npa")
            .build(),
        R.id.structuralUnits to Column.Builder()
            .title("Структурные единицы")
            .weightColumn(0.5f)
            .field("structuralUnits")
            .build(),
        R.id.punishments to Column.Builder()
            .title("Наказания")
            .weightColumn(0.5f)
            .field("punishments")
            .build()
    )
    @IgnoredOnParcel
    override var repositoryClass: Class<*> = ResponsibilityRepository::class.java
}