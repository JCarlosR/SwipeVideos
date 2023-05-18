package com.programacionymas.swipevideos.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.EventLogger
import com.programacionymas.swipevideos.MyApp
import com.programacionymas.swipevideos.player.cache.MyCacheDataSourceProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


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
            debugLog("already prepared")
        } else {
            this.videoUri = videoUri
            debugLog("exoPlayer prepare")
            this.exoPlayer.prepare()
        }
    }

    private val surfaceHolderCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.d(TAG, "surfaceCreated")
        }

        override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.d(TAG, "surfaceDestroyed")
        }
    }

    private var isSurfaceViewSet = false

    fun setSurfaceView(surfaceView: SurfaceView) {
        if (isSurfaceViewSet) return

        isSurfaceViewSet = true

        this.exoPlayer.setVideoSurfaceView(surfaceView)
        surfaceView.holder.addCallback(surfaceHolderCallback)
    }

    fun onReady(callback: ()->Unit) {
        this.exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    Log.d(TAG, "Is playing!")
                    runBlocking {
                        delay(2000L)
                        callback()
                    }
                    this@MyPlayer.exoPlayer.removeListener(this)
                }
            }
            /*override fun onIsLoadingChanged(isLoading: Boolean) {
                if (isLoading) {
                    callback()
                    this@MyPlayer.exoPlayer.removeListener(this)
                }
            }*/
        })
    }

    fun play() {
        debugLog("play()")
        if (this.exoPlayer.isLoading) {
            this.exoPlayer.playWhenReady = true
        } else {
            this.exoPlayer.play()
        }
    }

    fun pause() {
        debugLog("pause()")
        this.exoPlayer.pause()
    }

    private fun debugLog(message: String) {
        Log.d(TAG, "[${hashCode()}] [$videoFileName] $message")
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

    override fun toString(): String {
        return "MyPlayer(hashCode=${this.hashCode()})"
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