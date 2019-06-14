package ru.npc_ksb.alfaknd.menu

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.npc_ksb.alfaknd.R


class MenuFragment : ListFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_menu, container)

        listAdapter = MenuAdapter(R.layout.row_menu_action_item, activity!!, MenuItems.values())

        return view
    }
}