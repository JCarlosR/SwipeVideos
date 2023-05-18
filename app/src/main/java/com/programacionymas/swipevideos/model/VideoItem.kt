package com.programacionymas.swipevideos.model

data class VideoItem(
    var url: String,
    var title: String,
    var firstFrame: String,
    var ready: Boolean = false
)