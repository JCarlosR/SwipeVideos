package com.programacionymas.swipevideos.ui.compose

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.programacionymas.swipevideos.data.VideoItemsList
import com.programacionymas.swipevideos.model.VideoItem
import com.programacionymas.swipevideos.ui.player.MyPlayer
import com.programacionymas.swipevideos.ui.ui.theme.SwipeVideosTheme

/**
 * AndroidView allows us to hold regular views (not related with Compose),
 * but we must indicate it how to update its state on tree updates:
 * https://foso.github.io/Jetpack-Compose-Playground/viewinterop/androidview/
 */
@Composable
fun VideoPlayer(videoItem: VideoItem, myPlayer: MyPlayer? = null) {
    val context = LocalContext.current

    Box {
        // player view
        DisposableEffect(
            AndroidView(
                factory = {
                    // exo player view for our video player
                    PlayerView(context).apply {
                        player = myPlayer?.exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                    }
                },
                update = {
                    it.player = myPlayer?.exoPlayer
                }
            )
        ) {
            onDispose {
                // do not release player because we reuse same 3 instances
                Log.d(TAG, "onDispose ${videoItem.title}")
            }
        }

        // video title
        VideoTitle(title = videoItem.title)
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    SwipeVideosTheme {
        VideoPlayer(
            videoItem = VideoItemsList.get()[0]
        )
    }
}
