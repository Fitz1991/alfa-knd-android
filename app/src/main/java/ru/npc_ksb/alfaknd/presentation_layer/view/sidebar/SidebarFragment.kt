package ru.npc_ksb.alfaknd.presentation_layer.view.sidebar

import android.os.Bundle
import androidx.fragment.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.npc_ksb.alfaknd.R


class SidebarFragment : ListFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.f_sidebar, container)

        listAdapter = SidebarAdapter(R.layout.sidebar_item, activity!!, SidebarItems.values())

        return view
    }
}