package ru.npc_ksb.alfaknd.app.components.infoPanel

import android.app.Activity
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.npc_ksb.alfaknd.R


class InfoPanelAdapter(private val activity: Activity, private val items: List<ValueItem>) : ArrayAdapter<ValueItem>(activity, R.layout.values_list_item, items) {
    class ViewHolder {
        lateinit var txtTitle: TextView
        lateinit var txtValue: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val item = items[position]

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.values_list_item, null)!!
            val viewHolder = ViewHolder()
            view.tag = viewHolder
        }

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtValue = view.findViewById<TextView>(R.id.txtValue)

        txtTitle.text = item.title

        if (item.value == "") txtValue.text = "---"
        else txtValue.text = item.value

        return view
    }
}
