package com.lentimosystems.swipevideos.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.google.android.exoplayer2.SimpleExoPlayer
import com.lentimosystems.swipevideos.model.VideoItem

const val TAG = "VideoPager"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoPager(
    videos: List<VideoItem>,
    settledPage: Int,
    player: SimpleExoPlayer,
    nextPlayer: SimpleExoPlayer,
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
        VideoPlayer(
            videoItem = videos[page],
            exoPlayer = when (page) {
                settledPage -> {
                    player
                }
                settledPage + 1 -> {
                    nextPlayer
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