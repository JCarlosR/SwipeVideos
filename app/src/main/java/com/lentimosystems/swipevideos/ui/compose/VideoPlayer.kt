package com.lentimosystems.swipevideos.ui.compose

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.lentimosystems.swipevideos.ui.ui.theme.SwipeVideosTheme

@Composable
fun VideoPlayer(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // create our player
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            this.prepare()
        }
    }

    Box(modifier = modifier) {
        // video title
        Text(
            text = "Current Title",
            color = Color.White
        )

        // player view
        DisposableEffect(
            AndroidView(
                factory = {
                    // exo player view for our video player
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                    }
                }
            )
        ) {
            onDispose {
                // release player when no longer needed
                exoPlayer.release()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    SwipeVideosTheme {
        VideoPlayer()
    }
}
