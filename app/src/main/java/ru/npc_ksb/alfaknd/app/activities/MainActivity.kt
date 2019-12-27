package ru.npc_ksb.alfaknd.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.fragments.inspections.detail.DocumentTabFragment
import ru.npc_ksb.alfaknd.app.fragments.inspections.detail.InfoTabFragment
import ru.npc_ksb.alfaknd.app.fragments.inspections.detail.checklist.InspectionChecklistFragment
import ru.npc_ksb.alfaknd.app.interfaces.BasicActivity
import ru.npc_ksb.alfaknd.data.preferences.FirstSyncData
import ru.npc_ksb.alfaknd.data.preferences.FirstSyncData.isSync


class MainActivity : BasicActivity(), NavigationView.OnNavigationItemSelectedListener,
    InfoTabFragment.OnFragmentInteractionListener,
    InspectionChecklistFragment.OnFragmentInteractionListener,
    DocumentTabFragment.OnFragmentInteractionListener {

    lateinit var navController: NavController
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        val syncData = FirstSyncData.SyncPreference(this)
        if (!syncData.isSync) {
            startFirstSyncActivity()
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        drawerLayout = findViewById(R.id.drawer_layout)
        progressBar = findViewById(R.id.progress)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        actionBarDrawerToggle = toggle
        toggle.setToolbarNavigationClickListener {
            if (!toggle.isDrawerIndicatorEnabled)
                this.onBackPressed()
        }
    }

    override fun showHamburgerIcon() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
    }

    override fun showBackIcon() {
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun hideToolbarIcon(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
    }

    override fun setActionBarTitle(title: CharSequence) {
        supportActionBar?.title = title
    }

    override fun setActionBarSubTitle(title: CharSequence) {
        supportActionBar?.subtitle = title
    }

    override fun setNavState(menuId: Int) {
        navigationView.setCheckedItem(menuId)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> navController.navigate(R.id.profileFragment)
            R.id.nav_inspection -> navController.navigate(R.id.inspectionsFragment)
            R.id.nav_raid -> navController.navigate(R.id.raidsFragment)
            R.id.nav_prevention -> navController.navigate(R.id.preventionsFragment)
            R.id.nav_catalog -> navController.navigate(R.id.catalogFragment)
            R.id.nav_exit -> closeApp()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun startFirstSyncActivity() {
        val intent = Intent(this, FirstSyncActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun closeApp() {
        finishAndRemoveTask()
    }

    override fun onFragmentInteraction(uri: String) {
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}