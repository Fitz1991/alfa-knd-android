package ru.npc_ksb.alfaknd.app.components.table.column

import android.content.Context
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import ru.npc_ksb.alfaknd.app.components.table.interfaces.MapingView
import java.io.File

class ImageView(context: Context, _id: Int) : AppCompatImageView(context, null, 0),
    MapingView {
    init {
        this.id = _id
    }

    override fun setViewValue(value: String) {
        this.setImageURI(Uri.fromFile(File(value)))
        this.setPadding(10, 10, 10, 10)
    }
}