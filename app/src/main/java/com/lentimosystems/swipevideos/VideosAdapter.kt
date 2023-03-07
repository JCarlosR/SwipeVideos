package com.lentimosystems.swipevideos

import android.media.MediaPlayer
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

    private var currentItem: VideoViewHolder? = null
    private var incomingItem: VideoViewHolder? = null

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
        Log.d(TAG, "Binding position $position")
        holder.setVideoData(videoItems[position])
    }

    override fun getItemCount() = videoItems.size

    /**
     * As user starts swiping, this gets invoked for the next item.
     */
    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (currentItem == null) {
            // Set first video
            currentItem = holder
            holder.playWhenReady()
        } else {
            // Incoming video
            incomingItem = holder
            holder.videoView.pause()
        }

        Log.d(TAG, "onViewAttachedToWindow: ${holder.videoItem.videoTitle}")
    }

    /**
     * This gets invoked when the user completed the swipe, so previous item is detached.
     */
    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)

        currentItem?.videoView?.pause()
        incomingItem?.videoView?.start()

        currentItem = incomingItem

        Log.d(TAG, "onViewDetachedFromWindow: ${holder.videoItem.videoTitle}")
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var videoItem: VideoItem

        private var playWhenReady: Boolean = false

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

        fun playWhenReady() {
            this.playWhenReady = true
        }

        fun setVideoData(videoItem: VideoItem) {
            this.videoItem = videoItem

            txtTitle.text = videoItem.videoTitle
            txtDesc.text = videoItem.videoDesc
            videoView.setVideoPath(videoItem.videoURL)

            videoView.setOnPreparedListener { mediaPlayer ->
                progressBar.visibility = View.GONE

                if (playWhenReady) {
                    mediaPlayer.start()
                }

                val videoRatio = mediaPlayer.videoWidth / mediaPlayer.videoHeight.toFloat()
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