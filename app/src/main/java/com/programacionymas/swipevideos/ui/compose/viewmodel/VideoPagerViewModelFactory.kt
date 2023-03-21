package com.programacionymas.swipevideos.ui.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.programacionymas.swipevideos.player.cache.PreCacher
import com.programacionymas.swipevideos.ui.compose.VideoPagerViewModel

class VideoPagerViewModelFactory(
    private val preCacher: PreCacher
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        VideoPagerViewModel(preCacher) as T
}