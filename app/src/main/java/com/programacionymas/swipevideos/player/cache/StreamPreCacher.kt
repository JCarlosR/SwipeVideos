package com.programacionymas.swipevideos.player.cache

import com.programacionymas.swipevideos.model.VideoItem

/**
 * A PreCacher must allow caching videos, as well as cancelling all ongoing downloads.
 */
abstract class StreamPreCacher(private val cacheDataSourceProvider: CacheDataSourceProvider) {
    abstract fun precacheVideo(videoItem: VideoItem)
    abstract fun cancelAllDownloads()
}