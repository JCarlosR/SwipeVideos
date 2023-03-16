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
import com.programacionymas.swipevideos.ui.compose.VideoPager
import com.programacionymas.swipevideos.ui.compose.VideoPagerViewModel
import com.programacionymas.swipevideos.ui.ui.theme.SwipeVideosTheme

class MainActivity : ComponentActivity() {

    private val viewModel: VideoPagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        player = uiState.value.player.exoPlayer,
                        nextPlayer = uiState.value.nextPlayer.exoPlayer,
                        onPageSettled = viewModel::settlePage
                    )
                }
            }
        }
    }
}
