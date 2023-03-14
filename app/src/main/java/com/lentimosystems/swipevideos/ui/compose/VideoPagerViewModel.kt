package com.lentimosystems.swipevideos.ui.compose

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.lentimosystems.swipevideos.MyApp
import com.lentimosystems.swipevideos.data.VideoItemsList
import com.lentimosystems.swipevideos.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VideoPagerState(
    var settledPage: Int = 0,
    var videos: List<VideoItem> = VideoItemsList.get(),

    var player: SimpleExoPlayer = buildPlayer(),
    var nextPlayer: SimpleExoPlayer = buildPlayer()
)

private fun buildPlayer(): SimpleExoPlayer {
    return SimpleExoPlayer.Builder(MyApp.getAppContext()).build()
}

class VideoPagerViewModel : ViewModel() {
    // UI state
    private val _uiState = MutableStateFlow(VideoPagerState())
    val uiState: StateFlow<VideoPagerState>
        get() = _uiState.asStateFlow()

    fun settlePage(page: Int) {
        Log.d(TAG, "Settle page $page")

        _uiState.value.apply {
            this.player.setMediaItem(
                MediaItem.fromUri(this.videos[page].url)
            )
            this.player.playWhenReady = true
            this.player.prepare()

            // Already in last page
            val nextPage = page + 1
            if (nextPage >= this.videos.size) return

            this.nextPlayer.setMediaItem(
                MediaItem.fromUri(this.videos[nextPage].url)
            )
            this.nextPlayer.seekTo(INITIAL_FRAME_SEEK_MS)
            this.nextPlayer.prepare()
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
        private const val INITIAL_FRAME_SEEK_MS = 500L
    }
}