package ru.npc_ksb.alfaknd.domain.requests.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AuthQRResponse (
        @SerializedName("token")
        @Expose
        var token: String = "",

        @SerializedName("host")
        @Expose
        var host: String = ""
)
