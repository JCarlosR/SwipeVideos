package com.lentimosystems.swipevideos.ui.compose

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.tooling.preview.Preview
import com.lentimosystems.swipevideos.data.VideoItemsList
import com.lentimosystems.swipevideos.model.VideoItem
import com.lentimosystems.swipevideos.ui.ui.theme.SwipeVideosTheme

const val TAG = "VideoPager"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoPager(videos: List<VideoItem>) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        // Collect currentPage or settledPage from the snapshotFlow
        snapshotFlow { pagerState.settledPage }.collect { page ->
            Log.d(TAG, "Page settled to $page")
        }
    }

    HorizontalPager(pageCount = videos.size, state=pagerState) { page ->
        VideoPlayer(
            videoItem = videos[page],
            playWhenReady = page==0
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPagerPreview() {
    SwipeVideosTheme {
        VideoPager(
            VideoItemsList.get()
        )
    }
}