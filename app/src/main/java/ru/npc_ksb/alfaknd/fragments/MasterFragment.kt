package ru.npc_ksb.alfaknd.fragments

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import ru.npc_ksb.alfaknd.MenuActionItem
import ru.npc_ksb.alfaknd.MenuListAdapter
import ru.npc_ksb.alfaknd.R
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.content_menu.*


class MasterFragment : ListFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_menu, container)

        this.listAdapter = MenuListAdapter(R.layout.row_menu_action_item, this.activity!!, MenuActionItem.values())
        Toast.makeText(activity, "nhghnjcvfbvb", Toast.LENGTH_SHORT).show()
//        val list: ListView = list
//        list.adapter = listAdapter


//
//        val listView = this.findViewById<ListView>(android.R.id.list)
//
//        val menu_item = arrayOf(
//            resources.getStringArray(R.array.menu_icons),
//            resources.getStringArray(R.array.menu_icons),
//            resources.getStringArray(R.array.menu_icons),
//            resources.getStringArray(R.array.menu_icons)
//        )
//
//        val adapter = arrayListOf<MenuActionItem>()
//
//        listView.adapter = adapter
//
//        listView.onItemClickListener = AdapterView.OnItemClickListener { _, itemClicked, _, _ ->
//            Toast.makeText(
//                applicationContext, (itemClicked as TextView).text,
//                Toast.LENGTH_SHORT
//            ).show()
//        }


        return view
    }
}