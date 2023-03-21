package com.programacionymas.swipevideos.ui.compose

import android.util.Log
import androidx.lifecycle.ViewModel
import com.programacionymas.swipevideos.data.VideoItemsList
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.player.MyPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VideoPagerState(
    var settledPage: Int = 0,
    var videos: List<VideoItem> = VideoItemsList.get(),

    var prevPlayer: MyPlayer = MyPlayer(),
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

        val prevPage = page - 1
        val nextPage = page + 1

        _uiState.value.apply {
            if (this.player.isNewInstance && this.nextPlayer.isNewInstance) {
                Log.d(TAG, "Is first page settled")
                preparePlayerAndPlay(page)
            } else {
                this.player.pause()

                if (this.settledPage +1 == page) {
                    this.nextPlayer.play()
                    swapNext()
                } else if (this.settledPage -1 == page) {
                    this.prevPlayer.play()
                    swapPrev()
                }
            }
        }

        // Prepare the other players whenever possible
        prepareNextPlayer(nextPage)
        preparePrevPlayer(prevPage)

        _uiState.update {
            it.copy(settledPage = page)
        }
    }

    /**
     * Next player becomes now the active player.
     */
    private fun swapNext() {
        Log.d(TAG, "swapNext")

        _uiState.value.apply {
            val aux = this.prevPlayer
            this.prevPlayer = player
            this.player = nextPlayer
            this.nextPlayer = aux
        }
    }

    /**
     * Previous player becomes now the active player.
     */
    private fun swapPrev() {
        Log.d(TAG, "swapPrev")

        _uiState.value.apply {
            val aux = this.nextPlayer
            this.nextPlayer = player
            this.player = prevPlayer
            this.prevPlayer = aux
        }
    }

    /**
     * Prepare main player and start playing.
     */
    private fun preparePlayerAndPlay(position: Int) {
        _uiState.value.apply {
            this.player.prepare(
                videoUri = this.videos[position].url,
                playWhenReady = true
            )
        }
    }

    /**
     * Prepare previous player.
     */
    private fun preparePrevPlayer(position: Int) {
        if (!isValidPosition(position)) return

        _uiState.value.apply {
            this.prevPlayer.prepare(
                videoUri = this.videos[position].url,
                seekTo = INITIAL_FRAME_SEEK_MS
            )
        }
    }

    /**
     * Prepare next player.
     */
    private fun prepareNextPlayer(position: Int) {
        if (!isValidPosition(position)) return

        _uiState.value.apply {
            this.nextPlayer.prepare(
                videoUri = this.videos[position].url,
                seekTo = INITIAL_FRAME_SEEK_MS
            )
        }
    }

    private fun isValidPosition(position: Int) =
        (position >= 0 && position < _uiState.value.videos.size)

    companion object {
        private const val TAG = "VideoPagerViewModel"

        /**
         * Demo videos start with a black screen so we seek to this for the preview.
         */
        private const val INITIAL_FRAME_SEEK_MS = 1_000L
    }
}