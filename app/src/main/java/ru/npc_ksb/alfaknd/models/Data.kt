package ru.npc_ksb.alfaknd.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data{
    @SerializedName("data")
    @Expose
    val data: List<Datum>? = null
}

