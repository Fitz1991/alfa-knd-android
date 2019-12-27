package ru.npc_ksb.alfaknd.app.components.imageContents

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.npc_ksb.alfaknd.R

class CameraVideoAdapter(private val videoItems: List<InspectionVideo>, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<CameraVideoAdapter.VideoViewHolder>() {

    interface OnItemClickListener {
        fun onClickVideo(item: InspectionVideo)

        fun onLClickVideo(item: InspectionVideo)

        fun onClickVideoDel(item: InspectionVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {

        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.camera_video_item, parent, false)
        return VideoViewHolder(layoutView, videoItems, itemClickListener)
    }

    override fun getItemCount() = videoItems.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val item = videoItems[position]
        if (item.imagePath != "") {
            holder.videoImgView?.setImageURI(Uri.parse(item.imagePath))
            holder.onViewVisible(holder.videoDel, holder.videoPlay, View.VISIBLE)
        }
    }

    class VideoViewHolder(itemView: View, imageItems: List<InspectionVideo>, itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val _imageItems = imageItems
        val _itemClickListener = itemClickListener
        var videoImgView: ImageView? = null
        var videoDel: ImageView? = null
        var videoPlay: ImageView? = null


        init {
            videoImgView = itemView.findViewById(R.id.videoViewCamera)
            videoDel = itemView.findViewById(R.id.imageVideoDelete)
            videoPlay = itemView.findViewById(R.id.imageVideoPlay)

            val clickVideo = View.OnClickListener {
                val item = _imageItems[adapterPosition]
                if (item.imagePath == ""){
                    onViewVisible(videoDel, videoPlay, View.GONE)
                    _itemClickListener.onClickVideo(item)
                } else
                    _itemClickListener.onLClickVideo(item)
            }
            videoImgView?.setOnClickListener(clickVideo)


            val clickVideoDel = View.OnClickListener {
                val item = _imageItems[adapterPosition]
                _itemClickListener.onClickVideoDel(item)
            }
            videoDel?.setOnClickListener(clickVideoDel)
        }

        fun onViewVisible (view1: ImageView?, view2: ImageView?, visibility: Int){
            view1?.visibility = visibility
            view2?.visibility = visibility
        }
    }
}
