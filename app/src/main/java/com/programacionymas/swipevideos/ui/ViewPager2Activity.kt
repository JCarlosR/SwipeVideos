package com.programacionymas.swipevideos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.programacionymas.swipevideos.player.cache.PreCacher
import com.programacionymas.swipevideos.R
import com.programacionymas.swipevideos.data.VideoItemsList
import com.programacionymas.swipevideos.ui.adapter.VideosAdapter

class ViewPager2Activity : AppCompatActivity() {

    private val preCacher by lazy {
        PreCacher(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_pager_2)

        val videosViewPager = findViewById<ViewPager2>(R.id.viewPagerVideos)

        // By default the ViewPager binds only position 0 (position 1 after swiping)
        // With limit=1, it will bind position 0 and 1 at the beginning
        // Do we need to bind in advance? Or just precache the next video?
        videosViewPager.offscreenPageLimit = 1

        val adapter = VideosAdapter(preCacher)
        adapter.setVideoItems(VideoItemsList.get())

        videosViewPager.adapter = adapter
    }

}