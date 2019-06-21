package ru.npc_ksb.alfaknd

import android.util.Log
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class Session {
    enum class Status {
        INIT,
        ERROR,
        READY,
        PROGRESS,
        LOGGED
    }

    interface OnChangeStatusListener {
        fun onChangeStatus(status: Status)
    }

    companion object {
        private const val TAG = "Session"

        const val serverAddress = "http://192.168.43.39:3000"
        const val csrfHeader = "_csrftoken"
        const val sessionIDHeader = "_sessionid"
    }

    private var changeStatusListeners = ArrayList<OnChangeStatusListener>()
    private var status = Status.INIT
    private var csrfToken = ""
    private var sessionID = ""


    fun getStatus(): Status {
        return this.status
    }

    fun getCSRFToken(): String {
        return this.csrfToken
    }

    fun getSessionID(): String {
        return this.sessionID
    }


    fun setOnChangeStatusListener(listener: OnChangeStatusListener) {
        this.changeStatusListeners.add(listener)
    }

    fun remOnChangeStatusListener(listener: OnChangeStatusListener) {
        this.changeStatusListeners.remove(listener)
    }

    private fun onChangeStatus(status: Status) {
        this.status = status
        changeStatusListeners.forEach { listener ->
            listener.onChangeStatus(status)
        }
    }


    private fun getCSRFToken(callback: (token: String) -> Unit) {
        onChangeStatus(Status.PROGRESS)

        val client = OkHttpClient()
        val request = Request.Builder().url("$serverAddress/api/account/device-login/").get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onChangeStatus(Status.ERROR)
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
                        onChangeStatus(Status.READY)
                    }
                }
            }
        })
    }

    fun authCheck() {
        if (sessionID == "") {
            return
        }

        val client = OkHttpClient()

        val request = Request.Builder().url("$serverAddress/api/account/login-check/")
            .addHeader("Cookie", "$sessionIDHeader=$sessionID")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("qwerty", e.message)
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.code() != 200) {
                    exitProcess(0)
                }
            }
        })
    }

    fun startWorker() {
        /*getCsrfToken { token ->
            csrfToken = token
            Log.d("qwerty", "token: $token")
        }

        val authChecker = object : TimerTask() {
            override fun run() {
                authCheck()
            }
        }
        val timer = Timer()
        timer.schedule(authChecker, 5000, 5000)*/
    }
}