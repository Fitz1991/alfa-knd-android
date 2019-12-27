package ru.npc_ksb.alfaknd.app.components.table.filter

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.components.table.interfaces.AbstractBaseTable
import java.util.*


class FilterAdapter(
    var filterItemSelectedListener: FilterItemSelectedListener,
    private val filterList: MutableMap<String, LinkedList<FilterItem>>,
    val table: AbstractBaseTable
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_VIEW_TYPE_FILTER = 0
    private val ITEM_VIEW_TYPE_SEARCH_FILTER = 1
    private var filterViewHolder: FilterViewHolder? = null
    private var searchFilterViewHolder: SearchFilterViewHolder? = null

    private var currentFilter: HashSet<FilterItem>? = null
    private var searchText: String? = null
    private var filterSelectState = mutableMapOf<Int, Int>()
    private var filterCount: Int = 0

    init {
        filterCount = if (table.searchFilter) filterList.size + 1 else filterList.size
    }

    interface FilterItemSelectedListener {
        fun onFilterItemSelected(
            currentFilter: HashSet<FilterItem>?,
            searchText: String?
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (table.searchFilter) {
            return if (position == filterCount - 1) ITEM_VIEW_TYPE_SEARCH_FILTER else ITEM_VIEW_TYPE_FILTER
        } else {
            return if (position == filterCount) ITEM_VIEW_TYPE_SEARCH_FILTER else ITEM_VIEW_TYPE_FILTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_VIEW_TYPE_FILTER) {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.filter, parent, false)
            return FilterViewHolder(itemView, this)
        }
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.search_filter, parent, false)
        return SearchFilterViewHolder(itemView, this)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, filterPosition: Int) {
        if (holder is FilterViewHolder) {
            filterViewHolder = holder
            filterViewHolder?.bindView(filterPosition)
        } else if (holder is SearchFilterViewHolder) {
            searchFilterViewHolder = holder
            searchFilterViewHolder?.bindView()
        }
    }

    private fun updateCurrentFilter(spinnerPosition: Int, position: Int): HashSet<FilterItem>? {
        var filterList = convertMapToList(this.filterList)
        val filterItem = filterList[spinnerPosition][position]
        if (position != 0) {
            if (currentFilter == null) currentFilter = hashSetOf()
            if (!currentFilter?.contains(filterItem)!! && !isSelectedElementTheSpinner(
                    spinnerPosition
                )
            ) {
                currentFilter?.add(filterItem)
                filterSelectState[spinnerPosition] = position
            } else if (currentFilter?.isNotEmpty()!! && filterSelectState.isNotEmpty()) {
                if (currentFilter?.contains(filterItem)!!) {
                    currentFilter?.add(filterItem)
                }
                if (isSelectedElementTheSpinner(spinnerPosition)) {
                    currentFilter?.remove(
                        filterList[spinnerPosition][filterSelectState[spinnerPosition]!!]
                    )
                    filterSelectState[spinnerPosition] = position
                    currentFilter?.add(filterItem)
                }
            }
        } else {
            val filterForRemove = currentFilter?.filter {
                it.field == filterItem.field
            }
            currentFilter?.remove(filterForRemove?.first())
            filterSelectState.remove(spinnerPosition)
        }
        return currentFilter
    }

    private fun isSelectedElementTheSpinner(spinnerPosition: Int): Boolean {
        return (filterSelectState[spinnerPosition] != null && filterSelectState[spinnerPosition] != 0)
    }


    override fun getItemCount(): Int {
        return filterCount
    }

    private fun convertMapToList(_filterList: MutableMap<String, LinkedList<FilterItem>>): LinkedList<LinkedList<FilterItem>> {
        val filterList = LinkedList<LinkedList<FilterItem>>()
        for ((_, value) in _filterList.entries) {
            filterList.add(value)
        }
        return filterList
    }

    inner class FilterViewHolder(
        var view: View,
        var filterAdapter: FilterAdapter
    ) : RecyclerView.ViewHolder(view) {
        var filterSpinner: Spinner = view.findViewById<View>(R.id.filter_spinner) as Spinner
        var filterLabel: TextView = view.findViewById<View>(R.id.desc_filter) as TextView

        fun bindView(spinnerPosition: Int) {
            filterLabel.text = filterList.keys.elementAt(spinnerPosition)
            val filterList = convertMapToList(filterList)
            val list = filterList[spinnerPosition].map {
                it.value
            }
            val adapter =
                ArrayAdapter(filterSpinner.context, android.R.layout.simple_spinner_item, list)
            filterSpinner.adapter = adapter
            filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val currentFilter = updateCurrentFilter(spinnerPosition, position)
                    filterAdapter.searchText = null
                    filterItemSelectedListener.onFilterItemSelected(
                        currentFilter,
                        filterAdapter.searchText
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    inner class SearchFilterViewHolder(
        var view: View,
        var filterAdapter: FilterAdapter
    ) : RecyclerView.ViewHolder(view) {

        private val searchTextHandler: TextView.OnEditorActionListener =
            TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    v.isCursorVisible = false
                    v.clearFocus()
                    val `in`: InputMethodManager? = getSystemService<InputMethodManager>(
                        view.context,
                        InputMethodManager::class.java
                    )
                    `in`?.hideSoftInputFromWindow(
                        v.applicationWindowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    // обработка нажатия Enter
                    search(v.text.toString())
                    return@OnEditorActionListener true
                }
                false
            }

        var editTextClickListener: View.OnClickListener = View.OnClickListener { v ->
            if (v.id == R.id.search_filter) {
                filterEditText.isCursorVisible = true
                filterEditText.requestFocus()
            }
        }

        private var filterEditText: EditText =
            view.findViewById<View>(R.id.search_filter) as EditText

        fun bindView() {
            filterEditText.setOnClickListener(editTextClickListener)
            filterEditText.setOnEditorActionListener(searchTextHandler)
        }

        private fun search(searchText: String) {
            filterAdapter.searchText = searchText
            currentFilter = null
            filterItemSelectedListener.onFilterItemSelected(currentFilter, filterAdapter.searchText)
        }
    }

}