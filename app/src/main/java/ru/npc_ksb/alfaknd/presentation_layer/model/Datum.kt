package ru.npc_ksb.alfaknd.presentation_layer.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime


class Datum {
    @SerializedName("uuid")
    @Expose
    var uuid: Int? = null

    @SerializedName("__str__ ")
    @Expose
    var str: String? = null

    @SerializedName("inn")
    @Expose
    var inn: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("ogrn")
    @Expose
    var ogrn: String? = null

    @SerializedName("subject_type")
    @Expose
    var subjectType: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("change_number")
    @Expose
    var changeNumber: Int? = null

//    @SerializedName("updated_at")
//    @Expose
//    var updatedAt: LocalDateTime? = null
}
