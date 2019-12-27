package ru.npc_ksb.alfaknd.app.components.table.pagination

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.npc_ksb.alfaknd.R

class PaginationAdapter(
    _paginationItems: MutableMap<String, ArrayList<Any>>,
    _listener: PaginationClickListener,
    _clickedPage: Int? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemViewList = mutableListOf<View>()
    var clickedPage = _clickedPage
    private var paginationItems = _paginationItems

    interface PaginationClickListener {
        fun onPaginationClick(
            adapterPosition: Int
        )
    }

    private val listener = _listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView =
            LayoutInflater.from(parent.context).inflate(R.layout.pagination_item, parent, false)

        return ViewHolder(rowView, listener, paginationItems)
    }

    override fun getItemCount(): Int {
        return paginationItems.get(Paginator.LABELS)!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.bindView(position)
    }

    inner class ViewHolder(
        var view: View?,
        _listener: PaginationClickListener,
        paginationItems: MutableMap<String, ArrayList<Any>>
    ) : RecyclerView.ViewHolder(view!!) {


        val paginationCell = view?.findViewById(R.id.pagination_cell) as Button
        val listener = _listener

        init {
            if (paginationItems.get(Paginator.LABELS)!!.size < 2) {
                paginationCell.visibility = View.INVISIBLE
            }
            else{
                paginationCell.visibility = View.VISIBLE
                itemViewList.add(paginationCell)
                paginationCell.setOnClickListener {
                    val position =
                        paginationItems.get(Paginator.VALUES)!!.get(adapterPosition) as Int
                    listener.onPaginationClick(position)
                }
            }
        }

        @Suppress("DEPRECATION")
        fun bindView(position: Int) {
            val label = paginationItems.get(Paginator.LABELS)!!.get(position)
            paginationCell.text = label.toString()

            if (clickedPage !== null && (position == (itemCount - 1))) {
                var indexLabel: Int
                val indexValue = paginationItems.get(Paginator.VALUES)?.indexOf(clickedPage!!)

                itemViewList.forEach {
                    it as MaterialButton
                    if (((it.text as String) != "...")) {
                        val numPage = ((it).text as String).toInt()
                        indexLabel = paginationItems.get(Paginator.LABELS)?.indexOf(numPage)!!
                    } else {
                        indexLabel =
                            paginationItems.get(Paginator.LABELS)?.indexOf(it.text as String)!!
                    }
                    if (indexValue == indexLabel) {
                        it.backgroundTintList = ColorStateList.valueOf(view?.resources?.getColor(R.color.colorGreen)!!)
                        it.setTextColor(view!!.resources.getColor(R.color.colorButtonText))
                    }
                }
            }
        }
    }


}