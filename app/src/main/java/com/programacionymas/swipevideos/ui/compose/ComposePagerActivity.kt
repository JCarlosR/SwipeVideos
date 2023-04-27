package com.programacionymas.swipevideos.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.programacionymas.swipevideos.player.cache.PreCacher
import com.programacionymas.swipevideos.ui.compose.composable.VideoPager
import com.programacionymas.swipevideos.ui.compose.viewmodel.VideoPagerViewModel
import com.programacionymas.swipevideos.ui.compose.viewmodel.VideoPagerViewModelFactory
import com.programacionymas.swipevideos.ui.ui.theme.SwipeVideosTheme

class ComposePagerActivity : ComponentActivity() {

    private val viewModel: VideoPagerViewModel by viewModels {
        VideoPagerViewModelFactory(preCacher)
    }

    // TODO: Move to a background worker (?)
    private val preCacher by lazy {
        PreCacher(this)
    }

    /**
     * TODO: Stop precaching as user scrolls to the video, as ExoPlayer will take care of it
     */
    @OptIn(ExperimentalFoundationApi::class)
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
                        pagerState = viewModel.pagerState,

                        prevPlayer = uiState.value.prevPlayer,
                        currentPlayer = uiState.value.player,
                        nextPlayer = uiState.value.nextPlayer,

                        onPageSettled = viewModel::settlePage
                    )
                }
            }
        }
    }
}
