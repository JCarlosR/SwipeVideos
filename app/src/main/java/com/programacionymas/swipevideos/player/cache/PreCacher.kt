package com.programacionymas.swipevideos.player.cache

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.programacionymas.swipevideos.model.VideoItem

/**
 * Facade class that uses [Mp4PreCacher] and [HlsPreCacher] behind the scenes.
 */
class PreCacher(private val context: Context) {

    private val cacheDataSourceProvider by lazy {
        CacheDataSourceProvider(context)
    }

    private val cacheDataSource by lazy {
        cacheDataSourceProvider.cacheDataSource
    }

    private val cacheDataSourceFactory by lazy {
        cacheDataSourceProvider.cacheDataSourceFactory
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

    /**
     * Remove all cached content.
     */
    fun clearAll() {
        val cache = CacheProvider.get(context)

        cache.keys.forEach {
            cache.removeResource(it)
        }

        Log.d(TAG, "Cache space after clear: ${cache.cacheSpace}")
    }

    companion object {
        /**
         * Precache first 30 KB per video.
         */
        const val MAX_BYTES_PER_VIDEO = 30L * 1024

        private const val TAG = "PreCacher"
    }
}