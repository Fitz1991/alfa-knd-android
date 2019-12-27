package ru.npc_ksb.alfaknd.app.components.table.utils

import android.view.View
import android.widget.Button
import ru.npc_ksb.alfaknd.BuildConfig

class ButtonState {
    fun setDebugState(btn: Button) {
        if (BuildConfig.DEBUG) {
            btn.visibility = View.VISIBLE
        } else {
            btn.visibility = View.INVISIBLE
        }
    }
}