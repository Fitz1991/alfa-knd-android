package ru.npc_ksb.alfaknd.data_layer.repository.datasource

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime
import ru.npc_ksb.alfaknd.domain_layer.TiviTypeConverters

@Entity
class TypesOfResponsibility {
    @PrimaryKey
    var uuid: Int? = null


    var str: String? = null

    var inn: String? = null

    var address: String? = null

    var ogrn: String? = null

    var subjectType: Int? = null

    var name: String? = null


    var changeNumber: Int? = null

    @TypeConverters(TiviTypeConverters::class)
    var updatedAt: LocalDateTime? = null
}