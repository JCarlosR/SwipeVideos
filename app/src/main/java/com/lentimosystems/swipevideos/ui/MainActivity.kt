package com.lentimosystems.swipevideos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.lentimosystems.swipevideos.data.VideoItemsList
import com.lentimosystems.swipevideos.ui.compose.VideoPager
import com.lentimosystems.swipevideos.ui.ui.theme.SwipeVideosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videos = VideoItemsList.get()

        setContent {
            SwipeVideosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VideoPager(videos)
                }
            }
        }
    }
}
