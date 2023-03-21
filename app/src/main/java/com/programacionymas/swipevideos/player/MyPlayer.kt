package com.programacionymas.swipevideos.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.EventLogger
import com.programacionymas.swipevideos.MyApp
import com.programacionymas.swipevideos.player.cache.MyCacheDataSourceProvider


/**
 * Wrapper class.
 */
class MyPlayer {
    private val appContext: Context
        get() {
            return MyApp.getAppContext()
        }

    private val mediaSourceFactory = DefaultMediaSourceFactory(
        MyCacheDataSourceProvider(appContext).getDataSourceFactory()
    )

    val exoPlayer = SimpleExoPlayer.Builder(appContext)
        .setMediaSourceFactory(mediaSourceFactory)
        .build()

    /**
     * Considered new instance until preparing media.
     */
    var isNewInstance = true

    /**
     * For logging purposes.
     */
    var videoUri: String = ""

    init {
        addPlaybackStateListener()

        if (DEBUG) {
            enableDebugLogs()
        }
    }

    private fun enableDebugLogs() {
        val trackSelector = DefaultTrackSelector(appContext)
        val logger = EventLogger(trackSelector)
        exoPlayer.addAnalyticsListener(logger)
    }

    private val hlsMediaSource by lazy {
        HlsMediaSource.Factory(
            MyCacheDataSourceProvider(appContext).getDataSourceFactory()
        )
    }

    /**
     * Set media and prepare it.
     */
    fun prepare(videoUri: String, seekTo: Long = 0, playWhenReady: Boolean = false) {
        this.isNewInstance = false

        val mediaItem = MediaItem.fromUri(videoUri)

        if (videoUri.endsWith(".mp4")) {
            this.exoPlayer.setMediaItem(
                mediaItem
            )
        } else if (videoUri.endsWith(".m3u8")) {
            this.exoPlayer.setMediaSource(
                hlsMediaSource.createMediaSource(mediaItem)
            )
        }

        if (playWhenReady) {
            this.exoPlayer.playWhenReady = true
        }

        if (seekTo > 0) {
            this.exoPlayer.seekTo(seekTo)
        }

        if (this.videoUri == videoUri) {
            Log.d(TAG, "already prepared $videoUri")
        } else {
            Log.d(TAG, "prepare($videoUri)")
            this.exoPlayer.prepare()
        }

        this.videoUri = videoUri
    }

    fun play() {
        Log.d(TAG, "play() $videoUri")
        this.exoPlayer.play()
    }

    fun pause() {
        Log.d(TAG, "pause() $videoUri")
        this.exoPlayer.pause()
    }

    private fun addPlaybackStateListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        Log.d(TAG_STATE, "STATE_IDLE $videoUri")
                    }
                    Player.STATE_BUFFERING -> {
                        Log.d(TAG_STATE, "STATE_BUFFERING $videoUri")
                    }
                    Player.STATE_READY -> {
                        Log.d(TAG_STATE, "STATE_READY $videoUri | playWhenReady ${exoPlayer.playWhenReady}")
                        startBufferingUpdates()
                    }
                    Player.STATE_ENDED -> {
                        Log.d(TAG_STATE, "STATE_ENDED")
                    }
                }
            }
        })
    }

    private val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private var lastBufferPercentage: Int = -1

    private fun startBufferingUpdates() {
        mainHandler.postDelayed(bufferingRunnable, BUFFERING_UPDATES_INTERVAL)
    }

    /**
     * Periodic updates for the buffering, until completed.
     */
    private val bufferingRunnable = object : Runnable {
        override fun run() {
            if (exoPlayer.bufferedPercentage != lastBufferPercentage) {
                Log.d(TAG, "Buffer $videoFileName: position ${exoPlayer.bufferedPosition}, percentage ${exoPlayer.bufferedPercentage}")
                lastBufferPercentage = exoPlayer.bufferedPercentage
            }

            if (exoPlayer.bufferedPercentage < 100) {
                mainHandler.postDelayed(this, BUFFERING_UPDATES_INTERVAL)
            }
        }
    }

    private val videoFileName: String
        get() {
            return this.videoUri.substringAfterLast("/")
        }

    companion object {
        private const val TAG = "MyPlayer"
        private const val TAG_STATE = "ExoPlayerState"
        private const val DEBUG = false

        /**
         * In milliseconds.
         */
        private const val BUFFERING_UPDATES_INTERVAL = 2_000L
    }
}