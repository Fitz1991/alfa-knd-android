package ru.npc_ksb.alfaknd

import android.app.Application
import android.util.Log
import okhttp3.*
import java.io.IOException

class Application: Application() {
    companion object {
        var csrfTokenHeader = ""
        var sessionIdHeader = ""
    }

    fun getCsrfToken(callback: (token : String) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder().url("http://192.168.43.39:3000/api/account/device-login/").get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call : Call, e : IOException) {
                Log.d("qwerty", e.message)
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call : Call, response : Response) {
                val headers = response.headers().toString()
                Log.d("qwerty", headers)
                val cookies = response.headers().values("set-cookie")
                val pattern = "_csrftoken=([^;]+)".toRegex()
                cookies.forEach { cookie ->
                    val found = pattern.find(cookie)
                    if (found != null) {
                        callback(found.groups[1]!!.value)
                    }
                }
            }
        })
    }

    override fun onCreate() {
        getCsrfToken { token ->
            csrfTokenHeader = token
            Log.d("qwerty", "token: $token")
        }

        super.onCreate()
    }
}
