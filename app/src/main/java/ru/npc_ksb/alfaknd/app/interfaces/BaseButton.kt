package ru.npc_ksb.alfaknd.app.interfaces

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import ru.npc_ksb.alfaknd.R


@Suppress("DEPRECATION")
class BaseButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatButton(context, attrs, defStyleAttr){
    fun setDisabled(){
        this.isEnabled = false
        this.isClickable = false
        this.setBackgroundColor(resources.getColor(R.color.colorDisableBackgroundButton))
        this.setTextColor(resources.getColor(R.color.disableColorText))
    }
}