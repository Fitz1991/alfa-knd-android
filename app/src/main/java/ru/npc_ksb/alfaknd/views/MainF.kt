package ru.npc_ksb.alfaknd.views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SlidingPaneLayout
import android.util.Log
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import ru.npc_ksb.alfaknd.QRCodeActivity
import ru.npc_ksb.alfaknd.views.*
import ru.npc_ksb.alfaknd.sidebar.SidebarAdapter
import ru.npc_ksb.alfaknd.sidebar.SidebarItems

import ru.npc_ksb.alfaknd.R

class MainF : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater.inflate(R.layout.f_v_main,container,false)

        val sidebar = view.findViewById<SlidingPaneLayout>(R.id.sidebar_layout)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.mipmap.ic_alfaknd)
        setSupportActionBar(toolbar)

        val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawer_layout)
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

        val listView = view.findViewById<ListView>(android.R.id.list)
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

        val fc = view.findViewById<LinearLayout>(R.id.fragment_container)
        fc.setOnClickListener {
            if (sidebar.isOpen) {
                sidebar.closePane()
            }
        }

        // Return the fragment view/layout
        return view
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
        //return if (id == R.id.action_settings) {
        return super.onOptionsItemSelected(item)
    }

    fun onQRCodeClick(v: View) {
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
    }
    fun onTestClick(v: View) {
        ProgressDialog.show(this, "Loading", "Wait while loading...")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}
