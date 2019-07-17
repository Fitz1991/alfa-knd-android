package ru.npc_ksb.alfaknd.presentation_layer.presenter

import android.content.Context
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility
import ru.npc_ksb.alfaknd.domain_layer.InspectionsImpl
import ru.npc_ksb.alfaknd.presentation_layer.view.fragments.InspectionsFView

//связующее звено между view и model, получает данные из БД, вставляет во View
class InspectionsPresentorImpl(_view: InspectionsFView, fragment: Fragment, context: Context) : InspectionsPresentor {
    val view: InspectionsFView
    var context: Context? = null
    var fragment : Fragment?=null
    var inspectionsImpl = InspectionsImpl(fragment, context!!)


    lateinit var synchronizedData: MutableMap<String, Observable<MutableList<TypesOfResponsibility>>>

    init {
        this.fragment = fragment
        this.context = context
        this.view = _view
    }


    override fun requestSync(context: Context) {
        inspectionsImpl!!.sync()
        view.showInsertedData(inspectionsImpl.dataForInsert)
        view.showUpdatedData(inspectionsImpl.dataForUpdate)
    }
}