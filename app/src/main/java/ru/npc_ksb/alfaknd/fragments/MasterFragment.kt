package ru.npc_ksb.alfaknd.fragments

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.npc_ksb.alfaknd.MenuActionItem
import ru.npc_ksb.alfaknd.MenuListAdapter
import ru.npc_ksb.alfaknd.R


class MasterFragment : ListFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_menu, container)

        this.listAdapter = MenuListAdapter(R.layout.row_menu_action_item, this.activity!!, MenuActionItem.values())

        return view
    }
}