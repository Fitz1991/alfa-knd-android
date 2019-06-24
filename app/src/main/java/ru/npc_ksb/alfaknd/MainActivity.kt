package ru.npc_ksb.alfaknd

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val progress = this.findViewById<ProgressBar>(R.id.progressBar)
        progress.visibility = View.VISIBLE

        Session.activity = this

        Session.addAuthChangeListener {logged ->
            if (logged) {
                progress.visibility = View.GONE
                Log.d("mainma", "success")
                runOnUiThread {
                    Log.d("mainma", "success run")
                    val intent = Intent(this, WorkActivity::class.java)
                    startActivity(intent)
                    Log.d("mainma", "success start")
                }
            } else {
                progress.visibility = View.VISIBLE
            }
        }
        Session.start()
    }

    fun onScanCodeClick(v: View) {
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
    }
}