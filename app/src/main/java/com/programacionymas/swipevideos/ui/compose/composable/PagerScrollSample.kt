package com.programacionymas.swipevideos.ui.compose.composable

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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollToPageSample() {
    val pagerState = rememberPagerState()
    val scrollScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        // Collect from the snapshotFlow
        snapshotFlow { pagerState.settledPage }.collect { page ->
            println("Settled page: $page")
        }
    }

    Column {
        HorizontalPager(
            modifier = Modifier.height(400.dp),
            state = pagerState,
            pageCount = 10
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

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = {
                scrollScope.launch {
                    pagerState.scrollToPage(pagerState.currentPage + 1)
                }
            }) {
                Text(text = "Next Page")
            }
        }
    }
}

@Preview
@Composable
fun ScrollToPageSamplePreview() {
    ScrollToPageSample()
}