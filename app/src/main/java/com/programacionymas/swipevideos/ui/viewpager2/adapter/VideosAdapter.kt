package com.programacionymas.swipevideos.ui.viewpager2.adapter

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
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.programacionymas.swipevideos.player.cache.PreCacher
import com.programacionymas.swipevideos.R
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.player.cache.CacheDataSourceProvider
import com.programacionymas.swipevideos.ui.viewpager2.adapter.VideosAdapter.VideoViewHolder

/**
 * Accompanist is deprecated, will explore the Pager component: https://developer.android.com/jetpack/compose/layouts/pager
 * Note: Compose Pager is experiment.
 */
class VideosAdapter(private val preCacher: PreCacher) : RecyclerView.Adapter<VideoViewHolder>() {
    private var videoItems: List<VideoItem> = listOf()

    /**
     * Position of the item being displayed.
     */
    private var currentPosition = 0

    private var currentItem: VideoViewHolder? = null
    private var incomingItem: VideoViewHolder? = null

    fun setVideoItems(items: List<VideoItem>) {
        videoItems = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_videos_container, parent, false)

        return VideoViewHolder(view)
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
            // Start precaching
            currentPosition = 0
            preCacheNextVideo()
        } else {
            // Incoming video
            incomingItem = holder

            // Precache next video only if there is enough bandwidth
            // Make sure it doesn't overlap the caching process when the swiping finished
        }

        Log.d(TAG, "onViewAttachedToWindow: ${holder.videoItem.title}")
    }

    private fun preCacheNextVideo() {
        val nextPosition = currentPosition + 1

        // Reached the end of the playlist
        if (nextPosition >= videoItems.size) return

        val nextVideoUrl = videoItems[currentPosition + 1].url

        preCacher.precacheVideo(nextVideoUrl)
    }

    /**
     * This gets invoked when the user completed the swipe, so previous item is detached.
     */
    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)

        currentItem?.pause()
        incomingItem?.play()

        currentItem = incomingItem

        Log.d(TAG, "onViewDetachedFromWindow: ${holder.videoItem.title}")
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

        private fun setupTexts() {
            txtTitle.text = videoItem.title
            txtDesc.text = videoItem.url
        }

        private val mediaSourceFactory = DefaultMediaSourceFactory(
            CacheDataSourceProvider(itemView.context).cacheDataSourceFactory
        )

        private fun setupPlayer() {
            val player = SimpleExoPlayer.Builder(itemView.context)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()

            playerView.player = player
        }

        private fun prepareMedia() {
            Log.d(TAG, "prepareMedia")

            val mediaItem: MediaItem = MediaItem.fromUri(videoItem.url)
            val player = playerView.player ?: return

            player.setMediaItem(mediaItem)
            player.prepare()

            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_IDLE -> {
                            Log.d(TAG, "STATE_IDLE ${videoItem.title}")
                        }
                        Player.STATE_BUFFERING -> {
                            Log.d(TAG, "STATE_BUFFERING ${videoItem.title}")
                        }
                        Player.STATE_READY -> {
                            Log.d(TAG, "STATE_READY ${videoItem.title}, seeking $isSeeking")
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
                            Log.d(TAG, "STATE_ENDED ${videoItem.title}")
                        }
                    }
                }
            })
        }

        fun setVideoData(videoItem: VideoItem) {
            progressBar.isVisible = true

            this.videoItem = videoItem

            setupTexts()

            setupPlayer()

            prepareMedia()
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