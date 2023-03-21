package com.programacionymas.swipevideos.data

import com.programacionymas.swipevideos.model.VideoItem

object VideoItemsList {

    private val _items = ArrayList<VideoItem>()

    private val videoItems: ArrayList<VideoItem> by lazy {
        add(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            title = "Big Buck Bunny"
        )

        add(
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            title = "Elephants Dream"
        )

        add(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            title = "We Are Going On Bullrun"
        )

        add(
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            title = "For Bigger Fun"
        )

        _items
    }

    private fun add(url: String, title: String) {
        _items.add(
            VideoItem(url, title)
        )
    }

    fun get(): List<VideoItem> = videoItems
}