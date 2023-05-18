package com.programacionymas.swipevideos.ui.compose.viewmodel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import com.programacionymas.swipevideos.data.VideoItemsList
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.player.MyPlayer
import com.programacionymas.swipevideos.player.cache.PreCacher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VideoPagerState(
    var videos: List<VideoItem> = VideoItemsList.get(),

    var player: MyPlayer = MyPlayer()
)

@OptIn(ExperimentalFoundationApi::class)
class VideoPagerViewModel(
    private val preCacher: PreCacher
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(VideoPagerState())
    val uiState: StateFlow<VideoPagerState>
        get() = _uiState.asStateFlow()

    val pagerState = PagerState()

    /**
     * So we know the swipe direction.
     */
    private var previousPage = -1

    fun settlePage(page: Int) {
        Log.d(TAG, "Settle page $page")

        val pageBefore = page - 1
        val pageAfter = page + 1

        _uiState.value.apply {
            if (this.player.isNewInstance) {
                Log.d(TAG, "Is first page settled")
                preparePlayerAndPlay(page)
            } else {
                this.player.pause()
                preparePlayerAndPlay(page)
            }
        }

        // Prepare the other players whenever possible
        precachePosition(pageAfter)
        precachePosition(pageBefore)

        previousPage = page
    }

    private fun precachePosition(position: Int) {
        if (!isValidPosition(position)) return
        preCacher.precacheVideo(uiState.value.videos[position].url)
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

    private fun isValidPosition(position: Int) =
        (position >= 0 && position < _uiState.value.videos.size)

    companion object {
        private const val TAG = "VideoPagerViewModel"

        /**
         * Demo videos start with a black screen so we seek to this for the preview.
         */
        private const val INITIAL_FRAME_SEEK_MS = 100L
    }
}