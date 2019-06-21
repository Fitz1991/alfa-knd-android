package ru.npc_ksb.alfaknd

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SlidingPaneLayout
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import ru.npc_ksb.alfaknd.views.*
import ru.npc_ksb.alfaknd.sidebar.SidebarAdapter
import ru.npc_ksb.alfaknd.sidebar.SidebarItems


class WorkActivity : AppCompatActivity() {
    companion object {
        const val TAG = "WorkActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)

        val sidebar = findViewById<SlidingPaneLayout>(R.id.sidebar_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.mipmap.ic_alfaknd)
        setSupportActionBar(toolbar)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.sidebar_open,
            R.string.sidebar_close
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
                    changeFragment(InspectionsF())
                }
                SidebarItems.RAIDS.ordinal -> {
                    changeFragment(RaidsF())
                }
                SidebarItems.PREVENTIONS.ordinal -> {
                    changeFragment(PreventionsF())
                }
                SidebarItems.CATALOG.ordinal -> {
                    changeFragment(CatalogF())
                }
            }
        }

        toolbar.setOnClickListener {
            listView.adapter = SidebarAdapter(R.layout.sidebar_item, this, SidebarItems.values())
            changeFragment(DashboardF())
        }
        changeFragment(DashboardF())

        val fc = findViewById<LinearLayout>(R.id.fragment_container)
        fc.setOnClickListener {
            if (sidebar.isOpen) {
                sidebar.closePane()
            }
        }

        Session.addAuthChangeListener { logged: Boolean ->
            if (!logged) {
                finish()
            }
        }
    }


    private fun changeFragment(f: Fragment) {
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
        Log.d(TAG, item.toString())
        val id = item.itemId
        //return if (id == R.id.action_settings) {
        return super.onOptionsItemSelected(item)
    }
}