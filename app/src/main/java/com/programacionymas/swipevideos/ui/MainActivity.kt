package com.programacionymas.swipevideos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.programacionymas.swipevideos.data.VideoItemsList
import com.programacionymas.swipevideos.player.cache.PreCacher
import com.programacionymas.swipevideos.ui.compose.VideoPager
import com.programacionymas.swipevideos.ui.compose.VideoPagerViewModel
import com.programacionymas.swipevideos.ui.ui.theme.SwipeVideosTheme

class MainActivity : ComponentActivity() {

    private val viewModel: VideoPagerViewModel by viewModels()

    private val preCacher by lazy {
        PreCacher(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Move to a background worker, and invoke from viewModel
        preCacher.precacheVideo(VideoItemsList.get()[1].url)

        setContent {
            SwipeVideosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState = viewModel.uiState.collectAsState()

                    VideoPager(
                        videos = uiState.value.videos,
                        settledPage = uiState.value.settledPage,
                        prevPlayer = uiState.value.prevPlayer,
                        player = uiState.value.player,
                        nextPlayer = uiState.value.nextPlayer,
                        onPageSettled = viewModel::settlePage
                    )
                }
            }
        }
    }
}
