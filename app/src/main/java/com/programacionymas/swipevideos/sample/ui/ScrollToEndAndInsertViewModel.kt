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
    var buttonClicked: Boolean = false
)

@ExperimentalFoundationApi
class ScrollToEndAndInsertViewModel : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(
        UiState()
    )

    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    val pagerState = object : PagerState() {
        override val pageCount: Int
            get() {
                val computedPageCount = PAGES + if (uiState.value.buttonClicked) 1 else 0
                println("computedPageCount $computedPageCount")
                return computedPageCount
            }
    }

    fun settlePage(page: Int) {
        Log.d(TAG, "settlePage $page")
    }

    fun testScroll() {
        val lastPage = pagerState.pageCount - 1

        _uiState.value.buttonClicked = true

        viewModelScope.launch {
            pagerState.scrollToPage(lastPage)
        }
    }

    companion object {
        private const val TAG = "ScrollToEndAndInsert"
        private const val PAGES = 20
    }
}