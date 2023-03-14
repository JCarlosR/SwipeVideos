package com.lentimosystems.swipevideos.data

import com.lentimosystems.swipevideos.model.VideoItem

object VideoItemsList {
    fun get(): List<VideoItem> {
        val videoItems: MutableList<VideoItem> = ArrayList()

        val item = VideoItem(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            title = "Big Buck Bunny",
            description = "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself."
        )
        videoItems.add(item)

        val item2 = VideoItem(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            title = "We Are Going On Bullrun",
            description = "The Smoking Tire is going on the 2010 Bullrun Live Rally in a 2011 Shelby GT500, and posting a video from the road every single day!"
        )
        videoItems.add(item2)

        val item3 = VideoItem(
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            title = "Tears of Steel",
            description = "Tears of Steel was realized with crowd-funding by users of the open source 3D creation tool Blender."
        )
        videoItems.add(item3)

        return videoItems
    }
}