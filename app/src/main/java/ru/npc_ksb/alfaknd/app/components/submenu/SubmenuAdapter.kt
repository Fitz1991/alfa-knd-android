package ru.npc_ksb.alfaknd.app.components.submenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.npc_ksb.alfaknd.R


class SubmenuAdapter(private val submenuItems: List<SubmenuItem>, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<SubmenuAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SubmenuItem)
    }

    override fun getItemCount() = submenuItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.submenu_item, parent, false),
                submenuItems,
                itemClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = submenuItems[position]
        holder.submenuImage?.setImageResource(item.img)
        holder.submenuTitle?.text = item.title
    }

    class ViewHolder(
            itemView: View,
            submenuItems: List<SubmenuItem>,
            itemClickListener : OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val _submenuItems = submenuItems
        val _itemClickListener = itemClickListener
        var submenuImage: ImageView? = null
        var submenuTitle: TextView? = null

        init{
            submenuImage = itemView.findViewById(R.id.submenu_image)
            submenuTitle = itemView.findViewById(R.id.submenu_title)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val item = _submenuItems[adapterPosition]
            _itemClickListener.onItemClick(item)
        }
    }
}