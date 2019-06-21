package ru.npc_ksb.alfaknd

import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class Session {
    interface OnAuthChangeListener {
        fun onAuthChange(logged: Boolean)
    }

    companion object {
        private const val TAG = "Session"

        const val serverAddress = "http://192.168.43.39:3000"
        const val csrfHeader = "_csrftoken"
        const val sessionIDHeader = "_sessionid"

        private var changeListeners = ArrayList<OnAuthChangeListener>()
        private var csrfToken = ""
        private var sessionID = ""

        private var started = false


        fun setOnOnAuthChangeListener(listener: OnAuthChangeListener) {
            changeListeners.add(listener)
        }

        fun remOnOnAuthChangeListener(listener: OnAuthChangeListener) {
            changeListeners.remove(listener)
        }

        private fun onAuthChange(logged: Boolean) {
            changeListeners.forEach { listener ->
                listener.onAuthChange(logged)
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
                    val pattern = "${Session.sessionIDHeader}=([^;]+)".toRegex()
                    for (cookie in cookies) {
                        val found = pattern.find(cookie)
                        if (found != null) {
                            sessionID = found.groups[1]!!.value
                            callback(true)
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