package ru.npc_ksb.alfaknd.presentation_layer.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TypesOfResponsibility{
    @SerializedName("data")
    @Expose
    val data: List<Datum>? = null

    @SerializedName("meta")
    @Expose
    val meta: Meta? = null
}

