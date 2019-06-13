package ru.npc_ksb.alfaknd

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SlidingPaneLayout
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import ru.npc_ksb.alfaknd.fragments.BlankFragment0
import ru.npc_ksb.alfaknd.fragments.BlankFragment1
import ru.npc_ksb.alfaknd.fragments.BlankFragment2
import ru.npc_ksb.alfaknd.fragments.BlankFragment3


class MainActivity : AppCompatActivity() {

    var items: ArrayList<MenuActionItem>? = null
    private lateinit var listView: ListView
    private var adata = arrayOf("1", "2", "3","4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mSlidingLayout: SlidingPaneLayout = findViewById(R.id.sliding_pane_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        //val navView: FragmentActivity = findViewById(R.id.content_menu)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_alfaknd)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toolbar.setNavigationOnClickListener {
            if (mSlidingLayout.isOpen) {
                mSlidingLayout.closePane()
            } else {
                mSlidingLayout.openPane()
            }
        }
        /* ------------------------------------------------------------------------------------------------------ */

        val menuIcons = this.resources.obtainTypedArray(R.array.menu_icons)
        val menuItems = this.resources.obtainTypedArray(R.array.menu_items)

        listView = findViewById<View>(android.R.id.list) as ListView

        val adapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_list_item_1, adata
            )

        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    val textFragment = BlankFragment0()
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_container, textFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    //listView.setBackgroundColor(Color.TRANSPARENT)
                }
                1 -> {
                    val textFragment = BlankFragment1()
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_container, textFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                2 -> {
                    val textFragment = BlankFragment2()
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_container, textFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                3 -> {
                    val textFragment = BlankFragment3()
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_container, textFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
            }
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun onQRCodeClick(v: View) {
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
    }
}