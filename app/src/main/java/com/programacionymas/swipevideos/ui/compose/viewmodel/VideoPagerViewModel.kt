package com.programacionymas.swipevideos.ui.compose.viewmodel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import com.programacionymas.swipevideos.data.VideoItemsList
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.player.MyPlayer
import com.programacionymas.swipevideos.player.cache.Mp4PreCacher
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
    private val cacher: PreCacher
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(VideoPagerState())
    val uiState: StateFlow<VideoPagerState>
        get() = _uiState.asStateFlow()

    val pagerState = object : PagerState() {
        override val pageCount: Int
            get() = uiState.value.videos.size

    }

    /**
     * So we know the swipe direction.
     */
    private var previousPage = -1

    fun settlePage(page: Int) {
        Log.d(TAG, "Settle page $page")

        val pageBefore = page - 1
        val pageAfter = page + 1

        _uiState.value.apply {
            if (!this.player.isNewInstance) {
                this.player.pause()
            }
        }

        preparePlayerAndPlay(page)

        precachePosition(pageAfter)
        precachePosition(pageBefore)

        previousPage = page
    }

    private fun precachePosition(position: Int) {
        if (!isValidPosition(position)) return
        cacher.precacheVideo(uiState.value.videos[position])
    }


    /**
     * Prepare main player and start playing.
     */
    private fun preparePlayerAndPlay(position: Int) {
        setVideoReady(position, false)

        Log.d(TAG, "Starting to prepare ${_uiState.value.videos[position].title}")

        _uiState.value.player.prepare(
            videoItem = _uiState.value.videos[position],
            playWhenReady = true
        )

        _uiState.value.player.onReady {
            setVideoReady(position, true)
            Log.d(TAG, "Set ready=true to hide frame cover ${_uiState.value.videos[position].shortTitle}")
        }
    }

    private fun setVideoReady(position: Int, isReady: Boolean) {
        _uiState.update { oldState ->
            oldState.copy(
                videos = oldState.videos.toMutableList().apply {
                    this[position] = this[position].copy(ready = isReady)
                }
            )
        }
    }

    private fun isValidPosition(position: Int) =
        (position >= 0 && position < _uiState.value.videos.size)

    companion object {
        private const val TAG = "VideoPagerViewModel"
    }
}