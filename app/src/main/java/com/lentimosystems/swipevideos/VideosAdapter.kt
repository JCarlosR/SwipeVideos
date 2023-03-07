package com.lentimosystems.swipevideos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
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
        }

        Log.d(TAG, "onViewAttachedToWindow: ${holder.videoItem.videoTitle}")
    }

    /**
     * This gets invoked when the user completed the swipe, so previous item is detached.
     */
    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)

        currentItem?.pause()
        incomingItem?.play()

        currentItem = incomingItem

        Log.d(TAG, "onViewDetachedFromWindow: ${holder.videoItem.videoTitle}")
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var videoItem: VideoItem

        private var playWhenReady = false
        private var isSeeking = false

        var playerView: StyledPlayerView
        var txtTitle: TextView
        var txtDesc: TextView
        var progressBar: ProgressBar

        init {
            playerView = itemView.findViewById(R.id.playerView)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            txtDesc = itemView.findViewById(R.id.txtDesc)
            progressBar = itemView.findViewById(R.id.progressBar)
        }

        fun playWhenReady() {
            this.playWhenReady = true
        }

        fun play() {
            this.playerView.player?.let {
                // it.seekTo(0) // rewind then play
                it.play()
            }
        }

        fun pause() {
            this.playerView.player?.pause()
        }

        fun setVideoData(videoItem: VideoItem) {
            progressBar.isVisible = true

            this.videoItem = videoItem

            txtTitle.text = videoItem.videoTitle
            txtDesc.text = videoItem.videoDesc

            val player = SimpleExoPlayer.Builder(itemView.context).build()
            playerView.player = player

            // Set the media item to be played.
            val mediaItem: MediaItem = MediaItem.fromUri(videoItem.videoURL)
            player.setMediaItem(mediaItem)
            player.prepare()

            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_IDLE -> {}
                        Player.STATE_BUFFERING -> {
                            Log.d(TAG, "STATE_BUFFERING ${videoItem.videoTitle}")
                        }
                        Player.STATE_READY -> {
                            Log.d(TAG, "STATE_READY ${videoItem.videoTitle}, seeking $isSeeking")
                            progressBar.isVisible = false

                            if (playWhenReady) {
                                player.play()
                            } else if (!isSeeking) { // // Prevent infinite loop since seekTo invokes ready state
                                player.seekTo(INITIAL_FRAME_SEEK_MS)
                                isSeeking = true
                            } else {
                                isSeeking = false
                            }
                        }
                        Player.STATE_ENDED -> {
                            Log.d(TAG, "STATE_ENDED ${videoItem.videoTitle}")
                        }
                    }
                }
            })
        }
    }

    companion object {
        private const val TAG = "VideosAdapter"

        /**
         * High number since the demo videos start with a black screen.
         */
        private const val INITIAL_FRAME_SEEK_MS = 500L
    }
}