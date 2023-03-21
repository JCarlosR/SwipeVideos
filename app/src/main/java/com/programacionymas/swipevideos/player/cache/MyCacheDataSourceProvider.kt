package com.programacionymas.swipevideos.player.cache

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

/**
 * Provide a CacheDataSource, required by the `PreCacher`'s `CacheWriter`.
 * And its factory, required by `MyPlayer`.
 */
class MyCacheDataSourceProvider(private val context: Context) {

    private val httpDataSourceFactory by lazy {
        DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
    }

    private val cacheDataSourceFactory by lazy {
        CacheDataSource.Factory()
            .setCache(MyCacheProvider.get(context))
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
    }

    private val cacheDataSource by lazy {
        cacheDataSourceFactory.createDataSource()
    }

    fun getDataSourceFactory() = cacheDataSourceFactory

    fun getDataSource(): CacheDataSource {
        return cacheDataSource
    }

}