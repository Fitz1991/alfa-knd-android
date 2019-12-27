package ru.npc_ksb.alfaknd.app.fragments.catalog.responsibility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.components.infoPanel.EnityField
import ru.npc_ksb.alfaknd.app.components.infoPanel.InfoPanelAdapter
import ru.npc_ksb.alfaknd.app.components.infoPanel.InfoPanelItems
import ru.npc_ksb.alfaknd.app.interfaces.BasicFragment
import ru.npc_ksb.alfaknd.data.room.entities.Responsibility

class ResponsibilityInfoFragment : BasicFragment() {
    override var fragmentLayoutRId = R.layout.fragment_responsibiliry_info

    private var baseEnity: Responsibility? = null

    companion object {
        val INFO: String = "info"
    }

    override fun getToolbarTitleText(): CharSequence{
        return resources.getString(R.string.menu_responsibility)
    }

    override fun getToolbarSubTitleText(): CharSequence{
        baseEnity = arguments?.getSerializable(INFO) as Responsibility
        return baseEnity?.statement!!
    }

    override fun fragmentOnCreateView(
        view: View,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val valuesList = view.findViewById<ListView>(R.id.valuesList)

        val responsibility = baseEnity as Responsibility
        val items = InfoPanelItems.newValuesFromEnity(
            responsibility, listOf(
                EnityField("statement", "Формулировка нарушения"),
                EnityField("npa", "НПА"),
                EnityField("structuralUnits", "Структурные единицы"),
                EnityField("punishments", "Наказания")
            )
        )
        valuesList.adapter = InfoPanelAdapter(activity!!, items)
        return view
    }
}