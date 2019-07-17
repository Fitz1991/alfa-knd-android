package ru.npc_ksb.alfaknd.presentation_layer.view.activites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import ru.npc_ksb.alfaknd.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, WorkActivity::class.java)
        startActivity(intent)
        return


        val progress = this.findViewById<ProgressBar>(R.id.progressBar)
        progress.visibility = View.VISIBLE

        Session.activity = this

        Session.addAuthChangeListener { logged ->
            if (logged) {
                progress.visibility = View.GONE
                runOnUiThread {
                    val intent = Intent(this, WorkActivity::class.java)
                    startActivity(intent)
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