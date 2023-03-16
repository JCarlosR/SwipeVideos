package com.lentimosystems.swipevideos.ui.player

import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.lentimosystems.swipevideos.MyApp

/**
 * Wrapper class.
 */
object MyPlayer {
    fun build(playerTag: String): SimpleExoPlayer {
        val player = SimpleExoPlayer.Builder(MyApp.getAppContext())
            .build()

        player.addListener(object : Player.Listener {
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

        return player
    }
}