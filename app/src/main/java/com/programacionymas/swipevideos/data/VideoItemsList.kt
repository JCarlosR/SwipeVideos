package com.programacionymas.swipevideos.data

import com.programacionymas.swipevideos.model.VideoItem

/**
 * TODO: Make sure it works with HLS.
 * TODO: Precache completely Ads.
 */
object VideoItemsList {

    private val videoItems = ArrayList<VideoItem>()

    init {
        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-the-spheres-of-a-christmas-tree-2720-large.mp4",
            title = "Christmas"
        )

        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-tree-with-yellow-flowers-1173-large.mp4",
            title = "Flowers"
        )

        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-waves-in-the-water-1164-large.mp4",
            title = "Waves"
        )

        add(
            url = "https://d2ufudlfb4rsg4.cloudfront.net/newsnation/mkaNi6xbb/adaptive/mkaNi6xbb_master.m3u8",
            title = "Experts say Xi's visit is diplomatic cover"
        )

        add(
            url = "https://v.redd.it/8gl6r3cj1n0b1/DASH_720.mp4",
            title = "TikTok"
        )
    }

    private fun add(url: String, title: String) {
        videoItems.add(
            VideoItem(url, title)
        )
    }

    fun get(): List<VideoItem> = videoItems
}