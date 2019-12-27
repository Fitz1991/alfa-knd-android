package ru.npc_ksb.alfaknd.app.components.table.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jay.widget.StickyHeaders
import kotlinx.android.synthetic.main.cell_item.view.*
import kotlinx.android.synthetic.main.header_item.view.*
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.components.table.interfaces.AbstractBaseTable
import ru.npc_ksb.alfaknd.app.components.table.interfaces.MapingView
import timber.log.Timber

@Suppress("DEPRECATION")
class TableAdapter(
    private var table: AbstractBaseTable?,
    private var size: Int?,
    private var itemClickListener: ItemClickListener?,
    private var clickableColumns: List<Int>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaders {

    data class TableAdapterBuilder(
        var table: AbstractBaseTable? = null,
        var size: Int? = null,
        var itemClickListener: ItemClickListener? = null,
        var clickableColumns: List<Int>? = null
    ) {
        fun setTable(table: AbstractBaseTable?) = apply { this.table = table!! }
        fun size(size: Int) = apply { this.size = size }
        fun itemClickListener(itemClickListener: ItemClickListener?) =
            apply { this.itemClickListener = itemClickListener }

        fun clickableColumns(clickableColumns: List<Int>?) = apply { this.clickableColumns = clickableColumns }
        fun build() = TableAdapter(table, size, itemClickListener, clickableColumns)
    }

    interface ItemClickListener {
        fun onTableItemClick(position: Int, it: View)
    }

    companion object {
        val ITEM_VIEW_TYPE_HEADER = 0
        val ITEM_VIEW_TYPE_ITEM = 1
    }

    override fun isStickyHeader(position: Int): Boolean {
        return position == 0
    }

    override fun getItemCount(): Int {
        return size!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (isStickyHeader(position)) ITEM_VIEW_TYPE_HEADER else ITEM_VIEW_TYPE_ITEM
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType === ITEM_VIEW_TYPE_HEADER) {
            val headerView = LayoutInflater.from(parent.context).inflate(R.layout.header_item, parent, false)
            return HeaderHolder(
                headerView,
                table
            )
        }
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.cell_item, parent, false)
        val viewHolder = ViewHolder.ViewHolderBuilder<AbstractBaseTable>()
            .view(cellView)
            .setTable(table)
            .itemClickListener(itemClickListener)
            .clickableColumns(clickableColumns)
            .build()
        return viewHolder
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holder is HeaderHolder<*>) {
                val vh = holder as HeaderHolder<AbstractBaseTable>
                vh.bindView()
            } else if (holder is ViewHolder<*>) {
                val vh = holder as ViewHolder<AbstractBaseTable>

                vh.bindView(position)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    @SuppressLint("PrivateResource")
    class HeaderHolder<TS : AbstractBaseTable>(var view: View, var table: TS?) :
        RecyclerView.ViewHolder(view) {
        var header_layout = view.header_layout
        var textViewLayoutParams: LinearLayout.LayoutParams? = null

        init {
            header_layout.setBackgroundColor(view.resources.getColor(R.color.background_material_light))
            header_layout.orientation = LinearLayout.HORIZONTAL
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            header_layout.layoutParams = layoutParams

            table?.columns?.forEach { data ->
                val textView = TextView(view.context)
                textView.id = data.key
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, view.resources.getDimension(R.dimen.text_size))
                textView.setTypeface(textView.typeface, Typeface.BOLD)
                textView.setTextColor(view.resources.getColor(R.color.colorText))
                textView.setPadding(10, 10, 10, 10)
                textViewLayoutParams = TableRow.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, data.value.weightColumn!!
                )
                textViewLayoutParams!!.gravity = Gravity.CENTER
                textView.layoutParams = textViewLayoutParams
                header_layout.addView(textView)
            }
        }

        fun bindView() {
            table?.columns?.forEach { data ->
                val dataValue = if (data.value.title?.isNotEmpty()!!) data.value.title else ""
                view.findViewById<TextView>(data.key).text = dataValue
            }
        }
    }

    class ViewHolder<TS : AbstractBaseTable>(
        var view: View?,
        var table: TS?,
        var itemClickListener: ItemClickListener?,
        var clickableColumns: List<Int>?
    ) : RecyclerView.ViewHolder(view!!) {

        data class ViewHolderBuilder<TS : AbstractBaseTable>(
            var view: View? = null,
            var table: TS? = null,
            var itemClickListener: ItemClickListener? = null,
            var clickableColumns: List<Int>? = null
        ) {
            fun view(view: View?) = apply { this.view = view }
            fun setTable(table: TS?) = apply { this.table = table }
            fun itemClickListener(itemClickListener: ItemClickListener?) =
                apply { this.itemClickListener = itemClickListener }

            fun clickableColumns(clickableColumns: List<Int>?) = apply { this.clickableColumns = clickableColumns }
            fun build() = ViewHolder(view, table, itemClickListener, clickableColumns)
        }

        var tableLayout = view!!.table_layout
        var textViewLayoutParams: LinearLayout.LayoutParams? = null

        init {
            tableLayout.orientation = LinearLayout.HORIZONTAL

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.TOP
            }
            tableLayout.layoutParams = layoutParams

            table?.columnData?.forEach { data ->
                val tView = createView(data.key, view)
                if (tView!!.isClickable) {
                    tView.setOnClickListener {
                        itemClickListener?.onTableItemClick(adapterPosition, it)
                    }
                }
                table?.columns?.forEach {
                    if (data.key == it.key) {
                        textViewLayoutParams = TableRow.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, it.value.weightColumn!!
                        )
                    }
                }
                tView.layoutParams = textViewLayoutParams
                tableLayout.addView(tView)
            }
        }

        fun bindView(position: Int) {
            table?.columnData?.forEach { data ->
                var dataValue = ""
                if (data.value.size > 0) {
                    dataValue = if (data.value[position - 1]!!.isNotEmpty()) data.value[position - 1]!! else "---"
                }

                val mappingView: MapingView = view!!.findViewById<View>(data.key) as MapingView
                mappingView.setViewValue(dataValue)
            }
        }

        private fun createView(id: Int, view: View?): View? {
            var someView: View? = View(view?.context)
            table?.columns?.forEach { column ->
                if (column.key == id) {
                    someView = isClickable((column.value.viewClass.constructors.get(0).newInstance(view?.context, id)) as View)
                }
            }
            return someView
        }

        @Suppress("UNREACHABLE_CODE")
        private fun isClickable(view: View): View {
            try {
                if (clickableColumns?.contains(view.id)!!) {
                    view.isClickable = true
                    return view
                } else return view
            } catch (e: Exception) {
                return view
                Timber.d(e)
            }
        }

    }


}