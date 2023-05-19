package com.programacionymas.swipevideos.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun FirstFrameCover(
    isVisible: Boolean,
    firstFrameUrl: String,
    videoTitle: String
) {
    if (!isVisible) return

    AsyncImage(
        model = firstFrameUrl,
        contentDescription = videoTitle,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}