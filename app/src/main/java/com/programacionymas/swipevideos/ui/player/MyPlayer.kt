package com.programacionymas.swipevideos.ui.player

import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.programacionymas.swipevideos.MyApp

/**
 * Wrapper class.
 */
class MyPlayer(val playerTag: String) {
    val exoPlayer = SimpleExoPlayer.Builder(MyApp.getAppContext())
        .build()

    /**
     * Considered new instance until preparing media.
     */
    var isNewInstance = true

    init {
        addPlaybackStateListener()
    }

    /**
     * Set media and prepare it.
     */
    fun prepare(videoUri: String, seekTo: Long = 0, playWhenReady: Boolean = false) {
        isNewInstance = false

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
        Log.d(playerTag, "play()")
        this.exoPlayer.play()
    }

    fun pause() {
        Log.d(playerTag, "pause()")
        this.exoPlayer.pause()
    }

    private fun addPlaybackStateListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        Log.d(playerTag, "STATE_IDLE")
                    }
                    Player.STATE_BUFFERING -> {
                        Log.d(playerTag, "STATE_BUFFERING")
                    }
                    Player.STATE_READY -> {
                        Log.d(playerTag, "STATE_READY")
                    }
                    Player.STATE_ENDED -> {
                        Log.d(playerTag, "STATE_ENDED")
                    }
                }
            }
        })
    }
}