package ru.npc_ksb.alfaknd.app.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BasicFragment : Fragment() {
    abstract var fragmentLayoutRId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanseState: Bundle?): View? {
        val view = inflater.inflate(fragmentLayoutRId, container, false)
        setTitleText(getToolbarTitleText())
        setSubTitleText(getToolbarSubTitleText())
        showToolbarIcon()
        return fragmentOnCreateView(view, inflater, container, savedInstanseState)
    }

    abstract fun getToolbarTitleText(): CharSequence

    abstract fun getToolbarSubTitleText(): CharSequence

    abstract fun fragmentOnCreateView(view: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    fun setTitleText(text: CharSequence){
        (activity as BasicActivity).setActionBarTitle(text)
    }

    fun setSubTitleText(text: CharSequence){
        (activity as BasicActivity).setActionBarSubTitle(text)
    }

    open fun showToolbarIcon(){
        (activity as BasicActivity).showBackIcon()
    }

    fun showProgressBar(){
        (activity as BasicActivity).showProgressBar()
        (activity as BasicActivity).hideToolbarIcon()
    }

    fun hideProgressBar(){
        (activity as BasicActivity).hideProgressBar()
        showToolbarIcon()
    }

    fun closeApp(){
        activity!!.finishAndRemoveTask()
    }
}