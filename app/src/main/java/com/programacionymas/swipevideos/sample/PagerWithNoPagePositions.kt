package com.programacionymas.swipevideos.sample

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerWithNoPagePositions(
    prevPage: @Composable (()->Unit)?,
    currentPage: @Composable (()->Unit),
    nextPage: @Composable (()->Unit)?,
    onBackwardSwipe: ()->Unit,
    onForwardSwipe: ()->Unit
) {
    val pagerState = rememberPagerState(1) { 3 }
    val scrollEnabled = remember { mutableStateOf(true) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            if (page == 0) {
                onBackwardSwipe()
                pagerState.scrollToPage(1)
            } else if (page == 2) {
                onForwardSwipe()
                pagerState.scrollToPage(1)
            }
        }
    }

    scrollEnabled.value = (prevPage != null || nextPage != null)

    HorizontalPager(
        modifier = Modifier.height(400.dp),
        state = pagerState,
        userScrollEnabled = scrollEnabled.value
    ) { page ->
        if (page == 0 && prevPage != null) prevPage()
        else if (page == 1) currentPage()
        else if (page == 2 && nextPage != null) nextPage()
    }
}

@Preview
@Composable
fun PagerWithNoPagePositionsPreview() {
    PagerWithNoPagePositions(
        prevPage = {
            Text(text = "Previous")
        },
        currentPage = {
            Text(text = "Current")
        },
        nextPage = {
            Text(text = "Next")
        },
        onBackwardSwipe = {
            println("onBackwardSwipe")
        },
        onForwardSwipe = {
            println("onForwardSwipe")
        }
    )
}