package ru.npc_ksb.alfaknd

import android.os.Build
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.zxing.Result

import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler
import okhttp3.*
import java.io.IOException
import okhttp3.RequestBody


class QRCodeActivity : AppCompatActivity(), ResultHandler {
    companion object {
        private var REQUEST_CAMERA_CODE : Int = 1
    }

    private var scannerView: ZXingScannerView? = null

    fun checkPermissions() : Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_CODE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.scannerView = ZXingScannerView(this)

        setContentView(this.scannerView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkPermissions()) {
            } else {
                this.requestPermissions()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        /*if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Не разрешения для камеры", Toast.LENGTH_LONG).show()
            return
        }*/
        when (requestCode) {
            REQUEST_CAMERA_CODE -> {
                if (grantResults.count() > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            this.requestPermissions()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (checkPermissions()) {
            this.scannerView?.setResultHandler(this)
            this.scannerView?.startCamera()
        }
    }

    override fun onDestroy() {
        this.scannerView?.stopCamera()
        super.onDestroy()
    }

    override fun handleResult(result: Result) {
        val activity = this

        val client = OkHttpClient()

        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, """{ "token": "${result.text}" }""")
        val request = Request.Builder().url("http://192.168.43.39:3000/api/account/device-login/")
            .addHeader(
                Session.csfrTokenHeaderName,
                Session.csrfTokenHeader
            )
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call : Call, e : IOException) {
                Log.d("qwerty", e.message)
                call.cancel()

                activity.scannerView?.resumeCameraPreview(activity)
            }

            @Throws(IOException::class)
            override fun onResponse(call : Call, response : Response) {
                val cookies = response.headers().values("set-cookie")
                val pattern = "${Session.sessionIdCookieName}=([^;]+)".toRegex()
                cookies.forEach { cookie ->
                    val found = pattern.find(cookie)
                    if (found != null) {
                        Session.sessionIdCookie = found.groups[1]!!.value
                        Log.d("qwerty", "session: ${Session.sessionIdCookie}")
                    }
                }

                activity.finish()
            }
        })
    }
}
