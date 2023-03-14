package com.lentimosystems.swipevideos.ui.compose

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.tooling.preview.Preview
import com.lentimosystems.swipevideos.ui.ui.theme.SwipeVideosTheme

const val TAG = "VideoPager"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoPager() {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        // Collect from the snapshotFlow and read currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            Log.d(TAG, "Page changed to $page")
        }
    }

    HorizontalPager(pageCount = 10, state=pagerState) { page ->
        VideoPlayer()
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPagerPreview() {
    SwipeVideosTheme {
        VideoPager()
    }
}