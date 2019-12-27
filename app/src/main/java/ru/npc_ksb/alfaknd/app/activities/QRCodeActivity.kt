package ru.npc_ksb.alfaknd.app.activities

import android.os.Build
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result

import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler


class QRCodeActivity : AppCompatActivity(), ResultHandler {
    companion object {
        private const val REQUEST_CAMERA_CODE: Int = 1

        const val RESULT_CODE = 1
        const val RESULT_OK = 5
        const val RESULT_CANCEL = 6
    }

    private var scannerView: ZXingScannerView? = null

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
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

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("cancel", true)
        setResult(RESULT_CANCEL, intent)
        super.onBackPressed()
    }

    override fun handleResult(result: Result) {
        val intent = Intent()
        intent.putExtra("result", result.text)
        setResult(RESULT_OK, intent)
        finish()
        // scannerView?.resumeCameraPreview(this)
    }
}
