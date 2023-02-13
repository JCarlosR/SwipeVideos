package com.lentimosystems.swipevideos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.lentimosystems.swipevideos.VideosAdapter.VideoViewHolder

class VideosAdapter : RecyclerView.Adapter<VideoViewHolder>() {
    private var videoItems: List<VideoItem> = listOf()
    fun setVideoItems(items: List<VideoItem>) {
        videoItems = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_videos_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.setVideoData(videoItems[position])
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }

    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d(TAG, "onViewAttachedToWindow: ${holder.txtTitle}")
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d(TAG, "onViewDetachedFromWindow: ${holder.txtTitle}")
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoView: VideoView
        var txtTitle: TextView
        var txtDesc: TextView
        var progressBar: ProgressBar

        init {
            videoView = itemView.findViewById(R.id.videoView)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            txtDesc = itemView.findViewById(R.id.txtDesc)
            progressBar = itemView.findViewById(R.id.progressBar)
        }

        fun setVideoData(videoItem: VideoItem) {
            txtTitle.text = videoItem.videoTitle
            txtDesc.text = videoItem.videoDesc
            videoView.setVideoPath(videoItem.videoURL)

            videoView.setOnPreparedListener { mp ->
                progressBar.visibility = View.GONE
                mp.start()
                val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
                val screenRatio = videoView.width / videoView.height.toFloat()
                val scale = videoRatio / screenRatio
                if (scale >= 1f) {
                    videoView.scaleX = scale
                } else {
                    videoView.scaleY = 1f / scale
                }
            }
        }
    }

    companion object {
        private const val TAG = "VideosAdapter"
    }
}