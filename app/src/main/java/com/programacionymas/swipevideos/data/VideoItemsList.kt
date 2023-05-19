package com.programacionymas.swipevideos.data

import com.programacionymas.swipevideos.model.VideoItem

object VideoItemsList {

    private val videoItems = ArrayList<VideoItem>()

    init {
        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-the-spheres-of-a-christmas-tree-2720-large.mp4",
            title = "Christmas",
            firstFrame = "https://i.ibb.co/Yy4ynHT/christmas.jpg"
        )

        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-tree-with-yellow-flowers-1173-large.mp4",
            title = "Flowers",
            firstFrame = "https://i.ibb.co/Wz88cgs/flowers.jpg"
        )

        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-waves-in-the-water-1164-large.mp4",
            title = "Waves",
            firstFrame = "https://i.ibb.co/QrhBs1b/waves.jpg"
        )

        add(
            url = "https://d2ufudlfb4rsg4.cloudfront.net/newsnation/mkaNi6xbb/adaptive/mkaNi6xbb_master.m3u8",
            title = "Experts say Xi's visit is diplomatic cover",
            firstFrame = "https://i.ibb.co/fkkRx4K/nn-first.jpg"
        )

        add(
            url = "https://v.redd.it/8gl6r3cj1n0b1/DASH_720.mp4",
            title = "TikTok",
            firstFrame = "https://external-preview.redd.it/OidsuuDcHMOxYQVBM5SkrGWifoO47sg8_ROuPuFK7g8.png?format=pjpg&auto=webp&v=enabled&s=ae75797c535dfe21045d85b71ca41c2b10b7dbfe"
        )
    }

    private fun add(url: String, title: String, firstFrame: String) {
        videoItems.add(
            VideoItem(url, title, firstFrame)
        )
    }

    fun get(): List<VideoItem> = videoItems
}