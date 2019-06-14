package ru.npc_ksb.alfaknd

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.MenuItem

import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.content_menu.*


class MenuListAdapter(var resource: Int, var activity: Activity, items: Array<MenuActionItem>) :
    ArrayAdapter<MenuActionItem>(activity, resource, items) {

    private class MenuItemViewHolder {
        var menuItemImageView: ImageView? = null
        var menuItemTextView: TextView? = null
    }

    @SuppressLint("Recycle")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView

        if (rowView == null) {
            rowView = activity.layoutInflater.inflate(resource, null)

            val viewHolder = MenuItemViewHolder()

            viewHolder.menuItemImageView = rowView!!.findViewById(R.id.menu_item_image_view)
            viewHolder.menuItemTextView = rowView.findViewById(R.id.menu_item_text_view)

            rowView.tag = viewHolder
        }

        val holder = rowView.tag as MenuItemViewHolder

        val menuIcons = activity.resources.obtainTypedArray(R.array.menu_icons)
        val menuItems = activity.resources.obtainTypedArray(R.array.menu_items)

        for (item in MenuActionItem.values()) {
            if (position == item.ordinal) {
                holder.menuItemImageView!!.setImageDrawable(menuIcons.getDrawable(item.ordinal))
                holder.menuItemTextView!!.text =  menuItems.getString(item.ordinal)
                break
            }
        }

        return rowView
    }
}