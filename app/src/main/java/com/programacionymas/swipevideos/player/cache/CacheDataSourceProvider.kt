package com.programacionymas.swipevideos.player.cache

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

/**
 * Provide a CacheDataSource, required by the `PreCacher`'s `CacheWriter`.
 * And its factory, required by `MyPlayer`.
 */
class CacheDataSourceProvider(private val context: Context) {
    private val cache: Cache
        get() = CacheProvider.get(context)

    private val httpDataSourceFactory by lazy {
        DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
    }

    val cacheDataSourceFactory by lazy {
        CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            .setEventListener(object : CacheDataSource.EventListener {
                override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {
                    Log.d(TAG, "onCachedBytesRead. cacheSizeBytes: $cacheSizeBytes, cachedBytesRead: $cachedBytesRead")
                }
                override fun onCacheIgnored(reason: Int) {
                    Log.d(TAG, "onCacheIgnored. reason: $reason")
                }
            })
    }

    val cacheDataSource by lazy {
        cacheDataSourceFactory.createDataSource()
    }

    companion object {
        private const val TAG = "CacheDataSourceFactory"
    }

}