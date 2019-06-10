package ru.npc_ksb.alfaknd

import android.app.Application
import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess

class Application: Application() {
    companion object {
        const val csfrTokenHeaderName = "_csrftoken"
        const val sessionIdCookieName = "_sessionid"
        var csrfTokenHeader = ""
        var sessionIdCookie = ""
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
                val pattern = "$csfrTokenHeaderName=([^;]+)".toRegex()
                cookies.forEach { cookie ->
                    val found = pattern.find(cookie)
                    if (found != null) {
                        callback(found.groups[1]!!.value)
                    }
                }
            }
        })
    }

    fun authCheck() {
        if (sessionIdCookie == "") {
            return
        }

        val client = OkHttpClient()

        val request = Request.Builder().url("http://192.168.43.39:3000/api/account/login-check/")
            .addHeader("Cookie", "$sessionIdCookieName=$sessionIdCookie; USER_AUTHORITY_ID=1")
            .get()
            .build()
        //  .addHeader(csfrTokenHeaderName, csrfTokenHeader)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call : Call, e : IOException) {
                Log.d("qwerty", e.message)
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call : Call, response : Response) {
                if (response.code() != 200) {
                    exitProcess(0)
                }
            }
        })
    }

    override fun onCreate() {
        getCsrfToken { token ->
            csrfTokenHeader = token
            Log.d("qwerty", "token: $token")
        }

        val authChecker = object : TimerTask() {
            override fun run() {
                authCheck()
            }
        }
        val timer = Timer()
        timer.schedule(authChecker, 5000, 5000)

        super.onCreate()
    }
}
