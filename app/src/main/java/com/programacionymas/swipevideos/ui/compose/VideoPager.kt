package com.programacionymas.swipevideos.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.ui.player.MyPlayer

const val TAG = "VideoPager"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoPager(
    videos: List<VideoItem>,
    settledPage: Int,
    prevPlayer: MyPlayer,
    player: MyPlayer,
    nextPlayer: MyPlayer,
    onPageSettled: (page:Int)->Unit
) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        // Collect currentPage or settledPage from the snapshotFlow
        snapshotFlow { pagerState.settledPage }.collect { page ->
            onPageSettled(page)
        }
    }

    HorizontalPager(pageCount = videos.size, state = pagerState) { page ->
        // Log.d(TAG, "Composing VideoPlayer for page $page (settledPage $settledPage)")

        VideoPlayer(
            videoItem = videos[page],
            myPlayer = when (page) {
                settledPage -> {
                    player
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