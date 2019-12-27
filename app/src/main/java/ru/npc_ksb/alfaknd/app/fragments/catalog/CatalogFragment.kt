package ru.npc_ksb.alfaknd.app.fragments.catalog

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.activities.MainActivity
import ru.npc_ksb.alfaknd.app.components.submenu.SubmenuAdapter
import ru.npc_ksb.alfaknd.app.components.submenu.SubmenuItem
import ru.npc_ksb.alfaknd.app.interfaces.RootFragment

class CatalogFragment: RootFragment() {
    override var fragmentLayoutRId = R.layout.fragment_catalog
    override var menuRId = R.id.nav_catalog

    override fun fragmentOnCreateView(view: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val navController = Navigation.findNavController(activity as MainActivity, R.id.nav_host_fragment)

        val submenuItems = mutableListOf<SubmenuItem>()
        submenuItems.add(
                SubmenuItem(
                        R.drawable.responsibility,
                        "Виды ответственности",
                        R.id.action_catalogFragment_to_responsibilityFragment
                )
        )

        val submenuAdapter = SubmenuAdapter(submenuItems.toList(), object : SubmenuAdapter.OnItemClickListener {
            override fun onItemClick(item: SubmenuItem) {
                navController.navigate(item.destinationId)
            }
        })
        val submenuContainer = view.findViewById<RecyclerView>(R.id.submenu_container)
        val layoutManager = onScreenOrientationNumberColumns()
        layoutManager.orientation = RecyclerView.VERTICAL
        submenuContainer.adapter = submenuAdapter
        submenuContainer.layoutManager = layoutManager
        return view
    }

    override fun getToolbarTitleText(): CharSequence{
        return resources.getString(R.string.menu_catalog)
    }

    private fun onScreenOrientationNumberColumns(): GridLayoutManager {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            GridLayoutManager(context, 3)
        else GridLayoutManager(context, 2)

    }

}
