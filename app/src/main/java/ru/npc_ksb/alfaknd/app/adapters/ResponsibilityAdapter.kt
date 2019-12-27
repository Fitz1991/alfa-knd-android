package ru.npc_ksb.alfaknd.app.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.gson.Gson
import ru.npc_ksb.alfaknd.data.room.entities.Responsibility


class ResponsibilityAdapter(private val mContext: Context, private val items: List<Responsibility>) : ArrayAdapter<Responsibility>(mContext, android.R.layout.simple_list_item_1, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = TextView(mContext)
        val item = items.get(position)
        textView.text = Gson().toJson(item).toString()
        return textView
    }
}
