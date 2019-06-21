package ru.npc_ksb.alfaknd

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SlidingPaneLayout
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import ru.npc_ksb.alfaknd.views.*
import ru.npc_ksb.alfaknd.sidebar.SidebarAdapter
import ru.npc_ksb.alfaknd.sidebar.SidebarItems


class MainActivity : AppCompatActivity() {
    companion object {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Session.start()

        val progress = this.findViewById<ProgressBar>(R.id.progressBar)
        progress.visibility = View.VISIBLE
    }

    fun onScanCodeClick(v: View) {
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
    }
}