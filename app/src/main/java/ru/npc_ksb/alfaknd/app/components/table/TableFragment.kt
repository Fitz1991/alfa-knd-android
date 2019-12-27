package ru.npc_ksb.alfaknd.app.components.table

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.jay.widget.StickyHeadersLinearLayoutManager
import io.pravikant.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.fragment_table.*
import kotlinx.android.synthetic.main.fragment_table.view.*
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.components.table.adapters.TableAdapter
import ru.npc_ksb.alfaknd.app.components.table.filter.FilterAdapter
import ru.npc_ksb.alfaknd.app.components.table.filter.FilterItem
import ru.npc_ksb.alfaknd.app.components.table.interfaces.AbstractBaseTable
import ru.npc_ksb.alfaknd.app.components.table.interfaces.TableRepository
import ru.npc_ksb.alfaknd.app.components.table.pagination.PaginationAdapter
import ru.npc_ksb.alfaknd.app.components.table.pagination.Paginator
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.utils.Chunk
import java.util.*
import kotlin.reflect.full.declaredMemberProperties


@Suppress("UNCHECKED_CAST")
class TableFragment : Fragment(),
    PaginationAdapter.PaginationClickListener,
    TableAdapter.ItemClickListener, FilterAdapter.FilterItemSelectedListener {

    companion object {
        val ARG_TABLE_KEY = "table"
        val ARG_MODEL_KEY = "model"
        val ARG_CELL_CLICK_LISTENER_KEY = "cellClickListener"
    }

    data class Builder(
        var model: AndroidViewModel? = null,
        var table: AbstractBaseTable? = null,
        var cellClickListener: CellClickListener? = null
    ) {
        private var bundle: Bundle = Bundle()
        private var tableFragment = TableFragment()
        fun table(table: AbstractBaseTable) =
            apply { this.bundle.putParcelable(ARG_TABLE_KEY, table as Parcelable) }

        fun model(model: AndroidViewModel) =
            apply { this.bundle.putParcelable(ARG_MODEL_KEY, model as Parcelable) }

        fun cellClickListener(cellClickListener: CellClickListener) =
            apply {
                this.bundle.putParcelable(
                    ARG_CELL_CLICK_LISTENER_KEY,
                    cellClickListener as Parcelable
                )
            }

        fun build() = run {
            tableFragment.arguments = this.bundle
            return@run tableFragment
        }
    }

    private var searchText: String? = null
    private var isQuery: Boolean? = false
    private var currentPage: Int = 1
    private var recordsCount: Int = 0
    private var currentPageSize: Int? = null

    private lateinit var tableRepository: TableRepository<BaseEnity>
    private lateinit var table: AbstractBaseTable
    private lateinit var mainModel: MainModel
    private var cellClickListener: CellClickListener? = null
    var currentFilter: HashSet<FilterItem>? = hashSetOf()
    var initFilters: HashSet<FilterItem>? = hashSetOf()

    private lateinit var tableContainer: RecyclerView
    private lateinit var filterContainer: RecyclerView
    private lateinit var currentPageData: MutableMap<Int, BaseEnity>
    private lateinit var paginationDataInfo: MutableMap<Int, MutableMap<Int, Int>>

    private var paginator = Paginator()
    private lateinit var paginationItems: MutableMap<String, ArrayList<Any>>
    private lateinit var paginationPageButtonsContainer: RecyclerView
    private lateinit var paginationPageSizeSpinner: MaterialSpinner
    private lateinit var paginationInfoTextView: TextView
    private lateinit var paginationNextButton: MaterialButton
    private lateinit var paginationPreviousButton: MaterialButton
    private var notFoundTextView: TextView? = null

    interface CellClickListener : Parcelable {
        fun handleItemClick(
            baseEnity: BaseEnity,
            fragment: FragmentActivity?,
            it: View
        )
    }

    override fun onPaginationClick(
        adapterPosition: Int
    ) {
        updatePage(adapterPosition)
    }

    override fun onTableItemClick(position: Int, it: View) {
        val baseEnity = currentPageData.toList().get(position - 1).second
        if (cellClickListener != null) {
            cellClickListener?.handleItemClick(baseEnity, activity, it)
        }
    }

    override fun onFilterItemSelected(
        currentFilter: HashSet<FilterItem>?,
        searchText: String?
    ) {
        this.currentFilter?.clear()
        this.searchText = null
        if (currentFilter != null) {
            this.currentFilter?.addAll(currentFilter)
        }
        if (searchText != null) {
            this.searchText = searchText
        }
        setInitFilter()
        loadQueryData(this.currentPageSize!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args!!.containsKey(ARG_TABLE_KEY)) table =
            args.getParcelable<Parcelable>(ARG_TABLE_KEY) as AbstractBaseTable
        cellClickListener =
            if (args.containsKey(ARG_CELL_CLICK_LISTENER_KEY)) args.getParcelable<Parcelable>(
                ARG_CELL_CLICK_LISTENER_KEY
            ) as CellClickListener else null
        if (args.containsKey(ARG_MODEL_KEY)) mainModel =
            args.getParcelable<Parcelable>(ARG_MODEL_KEY) as MainModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanseState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tableRepository =
            mainModel.getRepository(table.repositoryClass) as TableRepository<BaseEnity>
        tableContainer = view.table_container
        filterContainer = view.filter_container
        paginationNextButton = view.pagination_next_button
        paginationPreviousButton = view.pagination_previous_button
        paginationPageButtonsContainer = view.pagination_page_buttons_container
        paginationInfoTextView = view.pagination_info_text_view

        setInitFilter()
        setLayoutParamsForTable(RecyclerView.VERTICAL)
        setLayoutParamsForPagination(RecyclerView.HORIZONTAL)
        setPaginationPageSize(2)

        paginationNextButton.setOnClickListener {
            if (currentPage.inc() > paginationDataInfo.size) {
                it.isEnabled = false
            } else {
                currentPage = currentPage.inc()
                updatePage(currentPage)
            }
        }

        paginationPreviousButton.setOnClickListener {
            if (currentPage.dec() < 1) {
                it.isEnabled = false
            } else {
                currentPage = currentPage.dec()
                updatePage(currentPage)
            }
        }

    }

    private fun setPaginationPageSize(selection: Int) {
        paginationPageSizeSpinner = view!!.pagination_page_size_spinner
        val paginationPageSizeAdapter = ArrayAdapter<String>(
            view!!.context, android.R.layout.simple_spinner_item,
            table.pageSizeOptions
        )
        paginationPageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paginationPageSizeSpinner.setAdapter(paginationPageSizeAdapter)
        paginationPageSizeSpinner.getSpinner().setSelection(selection)

        paginationPageSizeSpinner.getSpinner().onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentPage = 1
                    table.pageSizeOptions.get(position).toInt().let {
                        currentPageSize = it
                        when (isQuery) {
                            false -> loadData(it)
                            true -> loadQueryData(it)
                            else -> loadData(it)
                        }
                        setBaseFilterAdapter()
                    }
                }

                override fun onNothingSelected(arg0: AdapterView<*>) {}
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePage(
        page: Int
    ) {
        this.currentPage = page
        setPreviousEndNextButtonStyles(this.currentPage)
        this.paginationItems =
            this.paginator.getPagination(this.paginationDataInfo.size, this.currentPage)
        tableRepository.getPage(getOffset(), currentPageSize!!, currentFilter, searchText)
            .observe(viewLifecycleOwner, Observer {
                val currentPageData = mutableMapOf<Int, BaseEnity>()
                var i = 1
                it.forEach {
                    currentPageData.put(i, it)
                    i++
                }
                if (currentPageData.isNotEmpty()) {
                    paginationInfoTextView.text =
                        "Записи с ${this.paginationDataInfo.get(this.currentPage)!!.entries.first().value} до ${this.paginationDataInfo.get(
                            this.currentPage
                        )!!.entries.last().value} из ${this.recordsCount}"
                    table.columnData = setColumnData(currentPageData)!!
                    setTableAdapter()
                    setPaginationAdapter(this.paginationItems, this.currentPage)
                    renderVisibility()
                } else {
                    notFound(this.tableRepository)
                }
            })
    }

    private fun setInitFilter() {
        val columnsWithInitFilters = table.columns?.filter {
            it.value.initFilter != null
        }
        columnsWithInitFilters?.forEach { column ->
            val initFilterArr = resources.getStringArray(column.value.initFilter!!)
            initFilterArr.forEach {
                val filterItem = FilterItem(column.value.field!!, it)
                initFilters?.add(filterItem)
            }
        }
        currentFilter = initFilters!!
    }

    private fun setBaseFilterAdapter() {
        val baseFilterList = mutableMapOf<String, LinkedList<FilterItem>>()
        val columnsWithBaseFilters = table.columns?.filter {
            it.value.initFilter == null
        }?.filter {
            it.value.baseFilter != null
        }
        columnsWithBaseFilters?.forEach { column ->
            val baseFilterArr = resources.getStringArray(column.value.baseFilter!!)
            val itemList = LinkedList<FilterItem>()
            baseFilterArr.forEach {
                val filterItem = FilterItem(column.value.field!!, it)
                itemList.add(filterItem)
            }
            baseFilterList.put(column.value.title!!, itemList)
        }

        val filterAdapter = FilterAdapter(this, baseFilterList, table)
        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        filterContainer.layoutManager = layoutManager
        filterContainer.adapter = filterAdapter
    }

    private fun setPreviousEndNextButtonStyles(page: Int) {
        paginationPreviousButton.isEnabled = page != 1
        paginationItems = paginator.getPagination(paginationDataInfo.size, page)
        paginationNextButton.isEnabled =
            page != paginationItems[Paginator.VALUES]?.get(paginationItems[Paginator.VALUES]?.lastIndex!!)

        if (paginationPreviousButton.isEnabled) {
            paginationPreviousButton.iconTint =
                ColorStateList.valueOf(view?.context?.getColor(R.color.colorBackground)!!)
        } else {
            paginationPreviousButton.iconTint =
                ColorStateList.valueOf(view?.context?.getColor(R.color.md_grey_700)!!)
        }

        if (paginationNextButton.isEnabled) {
            paginationNextButton.iconTint =
                ColorStateList.valueOf(view?.context?.getColor(R.color.colorBackground)!!)
        } else {
            paginationNextButton.iconTint =
                ColorStateList.valueOf(view?.context?.getColor(R.color.md_grey_700)!!)
        }
    }

    fun setColumnData(currentPageData: MutableMap<Int, BaseEnity>): MutableMap<Int, MutableList<String?>>? {
        this.currentPageData = currentPageData
        val columnData: MutableMap<Int, MutableList<String?>>? = mutableMapOf()
        table.columns?.forEach {
            columnData?.put(it.key, getMatchFieldId(it.value.field!!, this.currentPageData))
        }
        return columnData
    }

    private fun setTableAdapter() {
        val tableAdapter = TableAdapter.TableAdapterBuilder()
            .setTable(table)
            .size(currentPageData.size + 1)
            .itemClickListener(this)
            .clickableColumns(table.clickableColumns)
            .build()
        tableContainer.adapter = tableAdapter
    }

    private fun setPaginationAdapter(
        paginationItems: MutableMap<String, ArrayList<Any>>,
        clickedPage: Int? = null
    ) {

        val paginationAdapter = PaginationAdapter(paginationItems, this, clickedPage)
        if (paginationItems.get(Paginator.LABELS)!!.size < 2) {
            view?.findViewById<View>(R.id.pagination_buttons)?.visibility = View.GONE
        } else {
            view?.findViewById<View>(R.id.pagination_buttons)?.visibility = View.VISIBLE
        }
        paginationPageButtonsContainer.adapter = paginationAdapter
    }

    private fun loadData(currentPageSize: Int) {
        isQuery = false
        tableRepository.getAllDataCount().observe(this, Observer {
            recordsCount = it
//            recordsCount = 0
            if (recordsCount != 0) {
                updatePaginationDataInfo(recordsCount, currentPageSize)
                updatePage(currentPage)
            } else {
                notFound(tableRepository)
            }
        })
    }

    private fun loadQueryData(currentPageSize: Int) {
        isQuery = true
        tableRepository.getPageCount(currentFilter, searchText)
            .observe(viewLifecycleOwner, Observer {
                recordsCount = it
                if (recordsCount != 0) {
                    updatePaginationDataInfo(recordsCount, currentPageSize)
                    updatePage(currentPage)
                } else {
                    notFound(tableRepository)
                }
            })
    }

    private fun updatePaginationDataInfo(recordsCount: Int, currentPageSize: Int) {
        paginationDataInfo = Chunk.chopped(recordsCount, currentPageSize)
    }

    private fun getOffset(): Int {
        return paginationDataInfo[currentPage]!!.keys.first() - 1
    }

    @SuppressLint("SetTextI18n")
    private fun notFound(tableRepository: TableRepository<BaseEnity>) {
        renderInvisibility()
        if (notFoundTextView == null) {
            val view = TextView(context)
            view.text = "${getString(R.string.not_found)}"
            view.id = View.generateViewId()
            notFoundTextView = view
        } else {
            table_view.removeView(notFoundTextView)
        }
        val set = ConstraintSet()
        table_view.addView(notFoundTextView!!, 0)
        set.clone(table_view)
        set.connect(notFoundTextView!!.id, ConstraintSet.TOP, table_view.id, ConstraintSet.TOP)
        set.connect(
            notFoundTextView!!.id,
            ConstraintSet.BOTTOM,
            table_view.id,
            ConstraintSet.BOTTOM
        )
        set.connect(notFoundTextView!!.id, ConstraintSet.LEFT, table_view.id, ConstraintSet.LEFT)
        set.connect(notFoundTextView!!.id, ConstraintSet.RIGHT, table_view.id, ConstraintSet.RIGHT)
        set.applyTo(table_view)
    }

    private fun renderInvisibility() {
        tableContainer.visibility = View.GONE
        pagination.visibility = View.GONE
    }

    private fun renderVisibility() {
        if (notFoundTextView != null) {
            table_view.removeView(notFoundTextView)
        }
        tableContainer.visibility = View.VISIBLE
        pagination.visibility = View.VISIBLE
    }


    private fun setLayoutParamsForTable(@RecyclerView.Orientation orientation: Int) {
        val mDivider = DividerItemDecoration(tableContainer.context, orientation)
        tableContainer.addItemDecoration(mDivider)
        val layoutManager = StickyHeadersLinearLayoutManager<TableAdapter>(this.context)
        layoutManager.scrollVerticallyBy(10, table_container.Recycler(), RecyclerView.State())
        layoutManager.orientation = orientation
        tableContainer.layoutManager = layoutManager
    }

    private fun setLayoutParamsForPagination(@RecyclerView.Orientation orientation: Int) {
        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = orientation
        paginationPageButtonsContainer.layoutManager = layoutManager
    }

    fun getMatchFieldId(field: String, data: MutableMap<Int, BaseEnity>):
            MutableList<String?> {
        val arrList = mutableListOf<String?>()

        data.map {
            it.value
        }.map {
            val obj = it
            it::class.declaredMemberProperties.filter {
                it.name == field
            }.map {
                if (it.getter.call(obj) != null) {
                    arrList.add(it.getter.call(obj).toString())
                } else {
                    arrList.add("")
                }
            }
        }
        return arrList
    }


}