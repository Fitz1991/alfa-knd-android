package ru.npc_ksb.alfaknd.presentation_layer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meta {
    @SerializedName("fields")
    @Expose
    var fields:List<Field>? = null
}