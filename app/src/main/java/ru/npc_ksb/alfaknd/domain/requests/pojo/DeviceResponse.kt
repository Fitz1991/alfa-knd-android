package ru.npc_ksb.alfaknd.domain.requests.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class DeviceResponse (
        @SerializedName("user_guid")
        @Expose
        var userGuid: String = "",

        @SerializedName("user_fio")
        @Expose
        var userFio: String = "",

        @SerializedName("user_email")
        @Expose
        var userEmail: String = "",

        @SerializedName("authority_guid")
        @Expose
        var authorityGuid: String = "",

        @SerializedName("authority_name")
        @Expose
        var authorityName: String = ""
)
