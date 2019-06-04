package ru.npc_ksb.alfaknd

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
       // val navView: NavigationView = findViewById(R.id.fragment_master)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
       // drawerLayout.addDrawerListener(toggle)
            //toggle.syncState()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        //noinspection SimplifiableIfStatement
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }


    fun onQRCodeClick(v: View) {
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
    }
}