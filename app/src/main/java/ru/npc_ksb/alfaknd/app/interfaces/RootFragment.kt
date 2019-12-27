package ru.npc_ksb.alfaknd.app.interfaces

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View


abstract class RootFragment : BasicFragment() {
    abstract var menuRId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanseState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanseState)
        (activity as BasicActivity).setNavState(menuRId)
        return view
    }

    override fun getToolbarSubTitleText(): CharSequence{
        return ""
    }

    override fun showToolbarIcon(){
        (activity as BasicActivity).showHamburgerIcon()
    }

}