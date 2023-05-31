package com.programacionymas.swipevideos.player.cache

import android.content.Context
import android.util.Log
import com.programacionymas.swipevideos.model.VideoItem

/**
 * Facade class that uses [Mp4PreCacher] and [HlsPreCacher] behind the scenes.
 */
class PreCacher(private val context: Context) {

    private val cacheDataSourceProvider by lazy {
        CacheDataSourceProvider(context)
    }

    private val mp4PreCacher: StreamPreCacher by lazy {
        Mp4PreCacher(cacheDataSourceProvider)
    }

    private val hlsPreCacher: StreamPreCacher by lazy {
        HlsPreCacher(cacheDataSourceProvider)
    }

    fun precacheVideo(videoItem: VideoItem) {
        if (videoItem.isHls) {
            hlsPreCacher.precacheVideo(videoItem)
        } else {
            mp4PreCacher.precacheVideo(videoItem)
        }
    }

    /**
     * Cancel ongoing downloads.
     */
    fun cancelAll() {
        mp4PreCacher.cancelAllDownloads()
        hlsPreCacher.cancelAllDownloads()
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
         * Precache first 2 MB per video.
         * Note: average segment size is 750kB ~ 1MB for hd720.
         */
        const val MAX_BYTES_PER_VIDEO = 2L * 1024 * 1024

        private const val TAG = "PreCacher"
    }
}