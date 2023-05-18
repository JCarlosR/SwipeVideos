package com.programacionymas.swipevideos.ui.compose.composable

import android.util.Log
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
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

    val context = LocalContext.current

    val surfaceView = remember {
        SurfaceView(context).apply {
            layoutParams =
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        }
    }

    
    Log.d(TAG, "Composing HorizontalPager")

    HorizontalPager(state = pagerState, beyondBoundsPageCount = 1) { page ->
        val videoItem = videos[page]
        val shouldFrameCover = (!videoItem.ready || page != pagerState.settledPage)

        Log.d(TAG, "Composing page $page, shouldFrameCover $shouldFrameCover")

        Box {
            if (page == pagerState.settledPage) {
                VideoPlayer(
                    videoItem = videoItem,
                    myPlayer = player,
                    surfaceView = surfaceView
                )
            }

            FirstFrameCover(
                isVisible = shouldFrameCover,
                firstFrameUrl = videoItem.firstFrame,
                videoTitle = videoItem.title
            )
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