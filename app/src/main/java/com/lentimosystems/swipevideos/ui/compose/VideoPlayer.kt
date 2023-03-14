package com.lentimosystems.swipevideos.ui.compose

import android.util.Log
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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.lentimosystems.swipevideos.data.VideoItemsList
import com.lentimosystems.swipevideos.model.VideoItem
import com.lentimosystems.swipevideos.ui.ui.theme.SwipeVideosTheme

@Composable
fun VideoPlayer(videoItem: VideoItem, playWhenReady: Boolean, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // create our player
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            this.setMediaItem(
                MediaItem.fromUri(videoItem.url)
            )
            this.playWhenReady = playWhenReady
            this.prepare()
            Log.d(TAG, "Preparing ${videoItem.title}")
        }
    }

    Box(modifier = modifier) {
        // player view
        DisposableEffect(
            AndroidView(
                factory = {
                    // exo player view for our video player
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
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
                Log.d(TAG, "onDispose ${videoItem.title}")
                // release player when no longer needed
                exoPlayer.release()
            }
        }

        // video title
        Text(
            text = videoItem.title,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    SwipeVideosTheme {
        VideoPlayer(
            videoItem = VideoItemsList.get()[0],
            playWhenReady = false
        )
    }
}
