package com.lentimosystems.swipevideos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.lentimosystems.swipevideos.PreCacher
import com.lentimosystems.swipevideos.R
import com.lentimosystems.swipevideos.model.VideoItem

class MainActivity : AppCompatActivity() {

    private val preCacher by lazy {
        PreCacher(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val videosViewPager = findViewById<ViewPager2>(R.id.viewPagerVideos)

        // By default the ViewPager binds only position 0 (position 1 after swiping)
        // With limit=1, it will bind position 0 and 1 at the beginning
        // Do we need to bind in advance? Or just precache the next video?
        videosViewPager.offscreenPageLimit = 1

        val adapter = VideosAdapter(preCacher)
        adapter.setVideoItems(videoItems)

        videosViewPager.adapter = adapter
    }

    private val videoItems: List<VideoItem>
        get() {
            val videoItems: MutableList<VideoItem> = ArrayList()

            val item = VideoItem()
            item.videoURL =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            item.videoTitle = "Big Buck Bunny"
            item.videoDesc =
                "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself."
            videoItems.add(item)

            val item2 = VideoItem()
            item2.videoURL =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
            item2.videoTitle = "We Are Going On Bullrun"
            item2.videoDesc =
                "The Smoking Tire is going on the 2010 Bullrun Live Rally in a 2011 Shelby GT500, and posting a video from the road every single day!"
            videoItems.add(item2)

            val item3 = VideoItem()
            item3.videoURL =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
            item3.videoTitle = "Tears of Steel"
            item3.videoDesc =
                "Tears of Steel was realized with crowd-funding by users of the open source 3D creation tool Blender."
            videoItems.add(item3)

            return videoItems
        }
}