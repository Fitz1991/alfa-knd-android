package ru.npc_ksb.alfaknd.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Field {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("required")
    @Expose
    var required: Boolean? = null
    @SerializedName("ordering")
    @Expose
    var ordering: String? = null
    @SerializedName("read_only")
    @Expose
    var readOnly: Boolean? = null
    @SerializedName("write_only")
    @Expose
    var writeOnly: Boolean? = null
    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("allow_null")
    @Expose
    var allowNull: Boolean? = null
    @SerializedName("allow_blank")
    @Expose
    var allowBlank: Boolean? = null
    @SerializedName("initial")
    @Expose
    var initial: String? = null

    @SerializedName("choices")
    @Expose
    var choices: List<List<String>>? = null

    @SerializedName("max_length")
    @Expose
    var maxLength: Int? = null
}