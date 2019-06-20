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
import android.text.Layout
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import ru.npc_ksb.alfaknd.fragments.*
import ru.npc_ksb.alfaknd.sidebar.SidebarAdapter
import ru.npc_ksb.alfaknd.sidebar.SidebarItems


class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sidebar = findViewById<SlidingPaneLayout>(R.id.sidebar_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.mipmap.ic_alfaknd)
        setSupportActionBar(toolbar)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toolbar.setNavigationOnClickListener {
            if (sidebar.isOpen) {
                sidebar.closePane()
            } else {
                sidebar.openPane()
            }
        }

        val listView = findViewById<ListView>(android.R.id.list)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                SidebarItems.INSPECTIONS.ordinal -> {
                    changeFragment(BlankFragment0())
                }
                SidebarItems.RAIDS.ordinal -> {
                    changeFragment(BlankFragment1())
                }
                SidebarItems.PREVENTIONS.ordinal -> {
                    changeFragment(BlankFragment2())
                }
                SidebarItems.CATALOG.ordinal -> {
                    changeFragment(BlankFragment3())
                }
            }
        }

        toolbar.setOnClickListener {
            listView.adapter = SidebarAdapter(R.layout.sidebar_item, this, SidebarItems.values())
            changeFragment(DashboardFragment())
        }
        changeFragment(DashboardFragment())

        val fc = findViewById<LinearLayout>(R.id.fragment_container)
        fc.setOnClickListener {
            if (sidebar.isOpen) {
                sidebar.closePane()
            }
        }
    }

    fun changeFragment(f: Fragment) {
        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.fragment_container, f)
        tx.addToBackStack(null)
        tx.commit()
        val sidebar = findViewById<SlidingPaneLayout>(R.id.sidebar_layout)
        if (sidebar.isOpen) {
            sidebar.closePane()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("qwerty", item.toString())
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun onQRCodeClick(v: View) {
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
    }
    fun onTestClick(v: View) {
        ProgressDialog.show(this, "Loading", "Wait while loading...")
    }
}