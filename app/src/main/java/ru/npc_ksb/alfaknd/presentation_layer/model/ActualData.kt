package ru.npc_ksb.alfaknd.presentation_layer.model

import androidx.room.*
import org.threeten.bp.LocalDateTime
import ru.npc_ksb.alfaknd.domain_layer.TiviTypeConverters

@Entity(tableName = "actualdata")
class ActualData(lastUpdated : LocalDateTime) {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0

    @TypeConverters(TiviTypeConverters::class)
    @ColumnInfo(name = "lastUpdated")
    var lastUpdated: LocalDateTime? = null

    init {
        this.lastUpdated = lastUpdated
    }

}