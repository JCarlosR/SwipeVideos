package com.programacionymas.swipevideos.ui.compose.composable

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
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
    player: MyPlayer,
    onPageSettled: (page:Int)->Unit
) {
    Log.d(TAG, "Composing VideoPager ${player.hashCode()}")

    LaunchedEffect(pagerState) {
        // Collect from the snapshotFlow
        snapshotFlow { pagerState.settledPage }.collect { page ->
            onPageSettled(page)
        }
    }
    
    Log.d(TAG, "Composing HorizontalPager")

    HorizontalPager(pageCount = videos.size, state = pagerState) { page ->
        Log.d(TAG, "Composing page $page, player ${player.hashCode()}")

        if (page == pagerState.settledPage) {
            VideoPlayer(
                videoItem = videos[page],
                myPlayer = player
            )   
        } else {
            Text(text = "Page $page")
        }
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