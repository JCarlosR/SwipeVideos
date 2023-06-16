package com.programacionymas.swipevideos.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.programacionymas.swipevideos.sample.ui.ScrollToEndAndInsertViewModel
import com.programacionymas.swipevideos.ui.ui.theme.SwipeVideosTheme

@OptIn(ExperimentalFoundationApi::class)
class ScrollToEndActivity : ComponentActivity() {

    private val viewModel: ScrollToEndAndInsertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeVideosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val uiState = viewModel.uiState.collectAsState()
                    ScrollToEndAndInsertSample(
                        pagerState = viewModel.pagerState,
                        buttonClicked = uiState.value.buttonClicked,
                        settlePage = viewModel::settlePage,
                        onTestButtonClick = viewModel::testScroll
                    )
                }
            }
        }
    }
}
