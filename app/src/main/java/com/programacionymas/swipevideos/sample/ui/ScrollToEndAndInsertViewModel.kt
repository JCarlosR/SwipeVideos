package com.programacionymas.swipevideos.sample.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    var buttonClicked: Boolean = false,
    var pageCount: Int
)

@ExperimentalFoundationApi
class ScrollToEndAndInsertViewModel : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(
        UiState(pageCount = INITIAL_PAGES)
    )

    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    val pagerState = object : PagerState() {
        override val pageCount: Int
            get() {
                return uiState.value.pageCount
            }
    }

    fun settlePage(page: Int) {
        Log.d(TAG, "settlePage $page")
    }

    fun testScroll() {
        val lastPage = pagerState.pageCount - 1

        _uiState.value.buttonClicked = true
        _uiState.value.pageCount += 1

        viewModelScope.launch {
            pagerState.scrollToPage(lastPage)
        }
    }

    companion object {
        private const val TAG = "ScrollToEndAndInsert"
        private const val INITIAL_PAGES = 20
    }
}