package ru.npc_ksb.alfaknd.app.components.imageContents

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.npc_ksb.alfaknd.R

class CameraImageAdapter(private val imageItems: List<InspectionImage>, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<CameraImageAdapter.ImaggeViewHolder>() {

    interface OnItemClickListener {
        fun onClickImage(item: InspectionImage)

        fun onLClickImage(item: InspectionImage)

        fun onClickImageDel(item: InspectionImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImaggeViewHolder {

        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.camera_image_item, parent, false)
        return ImaggeViewHolder(layoutView, imageItems, itemClickListener)
    }

    override fun getItemCount() = imageItems.size

    override fun onBindViewHolder(holder: ImaggeViewHolder, position: Int) {
        val item = imageItems[position]
        if (item.path != ""){
            holder.cameraImage?.setImageURI(Uri.parse(item.path))
            holder.imageDel?.visibility = View.VISIBLE
        }
    }

    class ImaggeViewHolder(itemView: View, imageItems: List<InspectionImage>, itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val _imageItems = imageItems
        val _itemClickListener = itemClickListener
        var cameraImage: ImageView? = null
        var imageDel: ImageView? = null


        init {
            cameraImage = itemView.findViewById(R.id.imageViewCamera)
            imageDel = itemView.findViewById(R.id.imageDelete)

            val clickImage  = View.OnClickListener{
                val item = _imageItems[adapterPosition]
                if (item.path == ""){
                    imageDel?.visibility = View.GONE
                    _itemClickListener.onClickImage(item)
                } else
                    _itemClickListener.onLClickImage(item)
            }
            cameraImage?.setOnClickListener(clickImage)


            val clickImageDel  = View.OnClickListener {
                val item = _imageItems[adapterPosition]
                _itemClickListener.onClickImageDel(item)
            }
            imageDel?.setOnClickListener(clickImageDel)
        }
    }
}