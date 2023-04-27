package com.programacionymas.swipevideos.ui.compose.composable

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.player.MyPlayer

const val TAG = "VideoPager"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoPager(
    videos: List<VideoItem>,
    pagerState: PagerState,
    prevPlayer: MyPlayer,
    currentPlayer: MyPlayer,
    nextPlayer: MyPlayer,
    onPageSettled: (page:Int)->Unit
) {
    Log.d(TAG, "Composing VideoPager ${prevPlayer.hashCode()}, ${currentPlayer.hashCode()}, ${nextPlayer.hashCode()}")

    LaunchedEffect(pagerState) {
        // Collect from the snapshotFlow
        snapshotFlow { pagerState.settledPage }.collect { page ->
            onPageSettled(page)
        }
    }

    fun getPlayer(page: Int, settledPage: Int): MyPlayer? {
        return when (page) {
            settledPage -> {
                currentPlayer
            }
            settledPage + 1 -> {
                nextPlayer
            }
            settledPage - 1 -> {
                prevPlayer
            }
            else -> {
                null
            }
        }
    }

    Log.d(TAG, "Composing HorizontalPager")

    HorizontalPager(pageCount = videos.size, state = pagerState) { page ->
        val player = getPlayer(page, pagerState.settledPage)

        Log.d(TAG, "Composing page $page, player ${player.hashCode()}")

        VideoPlayer(
            videoItem = videos[page],
            myPlayer = player
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun VideoPagerPreview() {
    SwipeVideosTheme {
        VideoPager(
            VideoItemsList.get()
        )
    }
}
*/