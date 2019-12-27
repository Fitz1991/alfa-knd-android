package ru.npc_ksb.alfaknd.app.fragments.catalog.responsibility

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.app.components.table.TableFragment
import ru.npc_ksb.alfaknd.app.components.table.column.Column
import ru.npc_ksb.alfaknd.app.components.table.interfaces.AbstractBaseTable
import ru.npc_ksb.alfaknd.app.interfaces.BasicFragment
import ru.npc_ksb.alfaknd.app.tables.ResponsibilityTable
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.data.repository.ResponsibilityRepository
import ru.npc_ksb.alfaknd.data.room.entities.Responsibility
import ru.npc_ksb.alfaknd.domain.sync.AuthoritySync

@SuppressLint("ParcelCreator")
@Parcelize
class ResponsibilityFragment : BasicFragment(), Parcelable {
    @IgnoredOnParcel
    private var deleteSyncDataBtn: Button? = null

    @IgnoredOnParcel
    private var syncResponsibilityBtn: Button? = null

    @IgnoredOnParcel
    override var fragmentLayoutRId = R.layout.fragment_responsibility

    @IgnoredOnParcel
    private var columns: MutableMap<Int, Column> = mutableMapOf()

    @IgnoredOnParcel
    private var mainModel: MainModel? = null

    @IgnoredOnParcel
    private var tableFragment: TableFragment? = null

    @IgnoredOnParcel
    private var fTrans: FragmentTransaction? = null

    @IgnoredOnParcel
    private lateinit var responsibilityTable: AbstractBaseTable

    override fun fragmentOnCreateView(view: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mainModel = MyApplication.getMainModel(activity!!)

        deleteSyncDataBtn = view.findViewById(R.id.deleteSyncData)
        syncResponsibilityBtn = view.findViewById(R.id.syncResponsibility)

        syncResponsibilityBtn?.setOnClickListener {
            AuthoritySync(mainModel!!).synchronize()
                    .doOnSubscribe {
                        setEnableButtons(false)
                        showProgressBar()
                    }
                    .doOnComplete {
                        setEnableButtons(true)
                        hideProgressBar()
                    }.subscribe()
        }

        deleteSyncDataBtn?.setOnClickListener {
            AuthoritySync(mainModel!!).cleanData()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        setEnableButtons(false)
                        showProgressBar()
                    }
                    .doOnComplete {
                        setEnableButtons(true)
                        hideProgressBar()
                    }
                    .subscribe()
        }

        columns = mutableMapOf(
            R.id.statement to Column.Builder()
                .title("Формулировка нарушения")
                .weightColumn(2.6f)
                .field("statement")
                .build()
            ,
            R.id.npa to Column.Builder()
                .title("НПА")
                .weightColumn(0.4f)
                .field("npa")
                .build()
        )


        responsibilityTable = ResponsibilityTable()
            .columns(columns)
            .clickableColumns(mutableListOf(R.id.statement, R.id.npa))
            .repositoryClass(ResponsibilityRepository::class.java)
            .searchFilter(true)
            .build()

        updateTable()
        return view
    }

    @Suppress("DEPRECATION")
    fun updateTable() {
        tableFragment = TableFragment.Builder()
            .table(responsibilityTable)
            .cellClickListener(ResponsibilityCellClickListener())
            .model(mainModel!!)
            .build()

        fTrans = fragmentManager?.beginTransaction()
        fTrans?.replace(R.id.responsibilityTableFrgm, tableFragment!!)
        fTrans?.commit()
    }

    override fun getToolbarTitleText(): CharSequence{
        return resources.getString(R.string.menu_catalog)
    }

    override fun getToolbarSubTitleText(): CharSequence{
        return resources.getString(R.string.menu_responsibility)
    }

    fun setEnableButtons(state: Boolean){
        deleteSyncDataBtn?.isEnabled = state
        syncResponsibilityBtn?.isEnabled = state
    }
}

@Parcelize
class ResponsibilityCellClickListener : TableFragment.CellClickListener {
    override fun handleItemClick(
        baseEnity: BaseEnity,
        fragment: FragmentActivity?,
        it: View
    ) {
        when (it.id) {
            R.id.statement -> {
                val bundle = Bundle()
                bundle.putSerializable(ResponsibilityInfoFragment.INFO, baseEnity)
                val navController: NavController =
                    Navigation.findNavController(fragment!!, R.id.nav_host_fragment)
                navController.navigate(R.id.responsibilityInfo, bundle)
            }
            R.id.npa -> {
                Toast.makeText(
                    it.context,
                    "НПА: ${(baseEnity as Responsibility).npa}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

}
