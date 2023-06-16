package com.programacionymas.swipevideos.sample

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfinitePagerSample() {
    val pageCount = remember { mutableIntStateOf(3) }

    val pagerState = rememberPagerState(pageCount = {
        return@rememberPagerState pageCount.value
    })

    LaunchedEffect(pagerState) {
        // Collect from the snapshotFlow
        snapshotFlow { pagerState.settledPage }.collect { page ->
            println("settledPage ${pagerState.settledPage}, pageCount ${pagerState.pageCount}")
            if (page == pagerState.pageCount - 1) {
                // adding one more page
                pageCount.value += 1
            }
        }
    }

    HorizontalPager(
        modifier = Modifier.height(400.dp),
        state = pagerState
    ) { page ->
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.LightGray)
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = page.toString(), fontSize = 32.sp)
        }
    }
}

@Preview
@Composable
fun InfinitePagerSamplePreview() {
    InfinitePagerSample()
}