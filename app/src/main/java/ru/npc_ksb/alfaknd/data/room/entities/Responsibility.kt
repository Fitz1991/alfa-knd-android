package ru.npc_ksb.alfaknd.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.NOCASE
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class Responsibility : BaseEnity(){

    var statement: String? = null

    @ColumnInfo(collate = NOCASE)
    var npa: String? = null

    @SerializedName("structural_units")
    var structuralUnits: String? = null

    var punishments: String? = null
}