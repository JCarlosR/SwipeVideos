package com.programacionymas.swipevideos.player.cache

import android.content.Context
import com.programacionymas.swipevideos.model.VideoItem

/**
 * Facade class that uses [Mp4PreCacher] and [HlsPreCacher] behind the scenes.
 */
class PreCacher(private val context: Context) {

    private val cacheDataSource by lazy {
        CacheDataSourceProvider(context).cacheDataSource
    }

    private val cacheDataSourceFactory by lazy {
        CacheDataSourceProvider(context).cacheDataSourceFactory
    }

    private val mp4PreCacher by lazy {
        Mp4PreCacher(cacheDataSource)
    }

    private val hlsPreCacher by lazy {
        HlsPreCacher(cacheDataSourceFactory)
    }

    fun precacheVideo(videoItem: VideoItem) {
        if (videoItem.isHls) {
            hlsPreCacher.precacheVideo(videoItem)
        } else {
            mp4PreCacher.precacheVideo(videoItem.url)
        }
    }

    companion object {
        /**
         * Precache first 30 KB per video.
         */
        const val MAX_BYTES_PER_VIDEO = 30L * 1024
    }
}