package ru.npc_ksb.alfaknd.app.interfaces

import androidx.appcompat.app.AppCompatActivity


abstract class BasicActivity : AppCompatActivity() {
    abstract fun showHamburgerIcon()
    abstract fun showBackIcon()
    abstract fun setNavState(menuId: Int)
    abstract fun setActionBarTitle(title:CharSequence)
    abstract fun setActionBarSubTitle(title:CharSequence)
    abstract fun showProgressBar()
    abstract fun hideProgressBar()
    abstract fun hideToolbarIcon()
}