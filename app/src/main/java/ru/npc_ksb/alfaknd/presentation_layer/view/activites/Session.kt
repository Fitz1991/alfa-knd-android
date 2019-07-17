package ru.npc_ksb.alfaknd.presentation_layer.view.activites

import android.app.Activity
import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class Session {
    companion object {
        private const val TAG = "Session"

        const val serverAddress = "http://192.168.43.112:3000"
        const val csrfHeader = "_csrftoken"
        const val sessionIDHeader = "_sessionid"

        private var changeListeners = ArrayList<(logged: Boolean) -> Unit>()
        private var csrfToken = ""
        private var sessionID = ""

        var activity: Activity? = null

        private var started = false


        fun addAuthChangeListener(listener: (logged: Boolean) -> Unit) {
            changeListeners.add(listener)
        }

        fun remAuthChangeListener(listener: (logged: Boolean) -> Unit) {
            changeListeners.remove(listener)
        }

        private fun onAuthChange(logged: Boolean) {
            activity!!.runOnUiThread {
                for (listener in changeListeners) {
                    listener(logged)
                }
            }
        }

        private fun getCSRFToken(callback: (token: String) -> Unit) {
            val client = OkHttpClient()
            val request = Request.Builder().url("$serverAddress/api/account/device-login/").get().build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, e.message)
                    call.cancel()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val cookies = response.headers().values("set-cookie")
                    val pattern = "$csrfHeader=([^;]+)".toRegex()
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
            val client = OkHttpClient()

            val request = Request.Builder().url("$serverAddress/api/account/login-check/")
                .addHeader("Cookie", "$sessionIDHeader=$sessionID")
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, e.message)
                    call.cancel()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (response.code() != 200) {
                        authLogout()
                    }
                }
            })
        }

        fun authLogout() {
            sessionID = ""
            csrfToken = ""
            onAuthChange(false)
        }

        fun authWithCode(code: String, callback: (success: Boolean) -> Unit) {
            val client = OkHttpClient()

            val json = MediaType.parse("application/json; charset=utf-8")
            val body = RequestBody.create(json, """{ "token": "$code" }""")
            val request = Request.Builder().url("$serverAddress/api/account/device-login/")
                .addHeader(csrfHeader, csrfToken)
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, e.message)
                    call.cancel()
                    callback(false)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val cookies = response.headers().values("set-cookie")
                    val pattern = "$sessionIDHeader=([^;]+)".toRegex()
                    for (cookie in cookies) {
                        val found = pattern.find(cookie)
                        if (found != null) {
                            sessionID = found.groups[1]!!.value
                            callback(true)
                            onAuthChange(true)
                            return
                        }
                    }
                    callback(false)
                }
            })
        }

        fun start() {
            if (started) return
            started = true

            val worker = object : TimerTask() {
                override fun run() {
                    if (csrfToken == "") {
                        getCSRFToken { token ->
                            csrfToken = token
                        }
                    } else if (sessionID != "") {
                        authCheck()
                    }
                }
            }
            val timer = Timer()
            timer.schedule(worker, 3000, 3000)
        }
    }
}