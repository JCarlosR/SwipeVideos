package com.programacionymas.swipevideos.ui.compose.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun VideoTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 22.sp
    )
}

@Preview
@Composable
fun VideoTitlePreview() {
    VideoTitle(
        title = "Video Title Example"
    )
}
