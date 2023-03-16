package com.programacionymas.swipevideos.data

import com.programacionymas.swipevideos.model.VideoItem

object VideoItemsList {

    private val _items = ArrayList<VideoItem>()

    private val videoItems: ArrayList<VideoItem> by lazy {
        add(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            title = "Big Buck Bunny",
            description = "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself."
        )

        add(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            title = "Tears of Steel",
            description = "Tears of Steel was realized with crowd-funding by users of the open source 3D creation tool Blender."
        )

        add(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            title = "We Are Going On Bullrun",
            description = "The Smoking Tire is going on the 2010 Bullrun Live Rally in a 2011 Shelby GT500, and posting a video from the road every single day!"
        )

        _items
    }

    private fun add(url: String, title: String, description: String) {
        _items.add(
            VideoItem(url, title, description)
        )
    }

    fun get(): List<VideoItem> = videoItems
}