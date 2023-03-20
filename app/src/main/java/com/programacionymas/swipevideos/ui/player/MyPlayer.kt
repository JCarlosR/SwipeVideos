package com.programacionymas.swipevideos.ui.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.EventLogger
import com.programacionymas.swipevideos.MyApp


/**
 * Wrapper class.
 */
class MyPlayer {
    private val appContext: Context
        get() {
            return MyApp.getAppContext()
        }

    val exoPlayer = SimpleExoPlayer.Builder(appContext)
        .build()

    /**
     * Considered new instance until preparing media.
     * TODO: Check if equivalent to IDLE state. Might be that fails to load and end up IDLE.
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

    /**
     * Set media and prepare it.
     */
    fun prepare(videoUri: String, seekTo: Long = 0, playWhenReady: Boolean = false) {
        this.isNewInstance = false
        this.videoUri = videoUri

        Log.d(TAG, "prepare($videoUri)")

        this.exoPlayer.setMediaItem(
            MediaItem.fromUri(videoUri)
        )

        if (playWhenReady) {
            this.exoPlayer.playWhenReady = true
        }

        if (seekTo > 0) {
            this.exoPlayer.seekTo(seekTo)
        }

        this.exoPlayer.prepare()
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

    private var lastBufferPercentage: Int = 0

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