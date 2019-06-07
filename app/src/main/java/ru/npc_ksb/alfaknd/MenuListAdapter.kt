package ru.npc_ksb.alfaknd

import android.app.Activity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


class MenuListAdapter(var resource: Int, var activity: Activity, items: Array<MenuActionItem>) :
    ArrayAdapter<MenuActionItem>(activity, resource, items) {

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

        when (position) {
            MenuActionItem.ITEM1.ordinal -> {
                holder.menuItemImageView!!.setImageDrawable(activity.getDrawable(R.drawable.ic_action_name))
                holder.menuItemTextView!!.text = activity.resources.getString(R.string.item1)
            }
            MenuActionItem.ITEM2.ordinal -> {
                holder.menuItemImageView!!.setImageDrawable(activity.getDrawable(R.drawable.ic_menu_manage))
                holder.menuItemTextView!!.text = activity.resources.getString(R.string.item2)
            }
            MenuActionItem.ITEM3.ordinal -> {
                holder.menuItemImageView!!.setImageDrawable(activity.getDrawable(R.drawable.ic_menu_send))
                holder.menuItemTextView!!.text = activity.resources.getString(R.string.item3)
            }
            MenuActionItem.ITEM4.ordinal -> {
                holder.menuItemImageView!!.setImageDrawable(activity.getDrawable(R.drawable.ic_menu_camera))
                holder.menuItemTextView!!.text = activity.resources.getString(R.string.item4)
            }
            MenuActionItem.ITEM5.ordinal -> {
                holder.menuItemImageView!!.setImageDrawable(activity.getDrawable(R.drawable.ic_menu_share))
                holder.menuItemTextView!!.text = activity.resources.getString(R.string.item5)
            }
        }
        return rowView
    }

    private class MenuItemViewHolder {
        var menuItemImageView: ImageView? = null
        var menuItemTextView: TextView? = null
    }
}