package ru.npc_ksb.alfaknd.app.components.table.column

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.components.table.interfaces.MapingView
import java.io.Serializable


@SuppressLint("ViewConstructor")
class TextView(context: Context?, _id: Int?) :
    Serializable, AppCompatTextView(context, null, 0),
    MapingView {

    init {
        this.id = _id!!
        setClickable()
    }

    @SuppressLint("RtlHardcoded")
    override fun setViewValue(value: String) {
        this.text = value
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setClickable()
        this.setBackgroundResource(outValue.resourceId)
        this.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(R.dimen.text_size)
        )
        this.gravity = Gravity.LEFT
        this.setPadding(10, 10, 10, 10)
    }

    @Suppress("DEPRECATION")
    private fun setClickable() {
        if (this.isClickable) {
            this.setTextColor(resources.getColor(R.color.md_blue_400))
        } else {
            this.setTextColor(resources.getColor(R.color.colorText))
        }
    }
}