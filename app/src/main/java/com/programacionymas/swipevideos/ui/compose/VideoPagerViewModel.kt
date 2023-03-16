package com.programacionymas.swipevideos.ui.compose

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.programacionymas.swipevideos.data.VideoItemsList
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.ui.player.MyPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VideoPagerState(
    var settledPage: Int = 0,
    var videos: List<VideoItem> = VideoItemsList.get(),

    var player: MyPlayer = MyPlayer(),
    var nextPlayer: MyPlayer = MyPlayer()
)

class VideoPagerViewModel : ViewModel() {
    // UI state
    private val _uiState = MutableStateFlow(VideoPagerState())
    val uiState: StateFlow<VideoPagerState>
        get() = _uiState.asStateFlow()

    fun settlePage(page: Int) {
        Log.d(TAG, "Settle page $page")

        val nextPage = page + 1

        _uiState.value.apply {
            if (this.player.isNewInstance && this.nextPlayer.isNewInstance) {
                Log.d(TAG, "First page settled")

                this.player.prepare(
                    videoUri = this.videos[page].url,
                    playWhenReady = true
                )

                // If it's last page
                if (nextPage >= this.videos.size) return

                this.nextPlayer.prepare(
                    videoUri = this.videos[nextPage].url,
                    seekTo = INITIAL_FRAME_SEEK_MS
                )
            } else {
                this.player.pause()
                this.nextPlayer.play()

                // Next player becomes now the active player
                val auxPlayer = this.player
                this.player = nextPlayer

                // We reuse the other player instance to load next video
                this.nextPlayer = auxPlayer

                // If it's last page
                if (nextPage >= this.videos.size) return

                this.nextPlayer.prepare(
                    videoUri = this.videos[nextPage].url,
                    seekTo = INITIAL_FRAME_SEEK_MS
                )
            }
        }

        _uiState.update {
            it.copy(settledPage = page)
        }
    }

    companion object {
        private const val TAG = "VideoPagerViewModel"

        /**
         * Demo videos start with a black screen so we seek to this for the preview.
         */
        private const val INITIAL_FRAME_SEEK_MS = 1_000L
    }
}