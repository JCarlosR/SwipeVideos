package com.lentimosystems.swipevideos

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * Questions to resolve:
 * - How much should we cache? Whether percentage wise or fixed initial seconds
 * - How do we prioritize current video loading over precaching? Is that actually needed?
 * - Do we need to manually check if a video is already cached before requesting it?
 */
class PreCacher(private val context: Context) {

    /**
     * The CacheEvictor determines when to delete cached files.
     * This one deletes the the least recently used cached file first, when there is no more space.
     */
    private val cacheEvictor by lazy {
        LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE_BYTES)
    }

    /**
     * Let ExoPlayer manage its own SQLite tables for caching.
     */
    private val databaseProvider by lazy {
        ExoDatabaseProvider(context)
    }

    private val simpleCache by lazy {
        SimpleCache(context.cacheDir, cacheEvictor, databaseProvider)
    }

    private val httpDataSourceFactory by lazy {
        DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
    }

    private val cacheDataSourceFactory by lazy {
        CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .createDataSource()
    }


    fun precacheVideo(videoUrl: String) {
        val videoUri = Uri.parse(videoUrl)
        val dataSpec = DataSpec(videoUri)

        val progressListener =
            CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                Log.d(TAG, "downloadPercentage $downloadPercentage | video: $videoUri")
            }

        preCacheOnAnotherThread(dataSpec, progressListener)
    }

    /**
     * @param dataSpec what to cache
     * @param progressListener listen download progress
     */
    private fun preCacheOnAnotherThread(dataSpec: DataSpec, progressListener: CacheWriter.ProgressListener) {
        val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.execute {
            runCatching {
                CacheWriter(
                    cacheDataSourceFactory,
                    dataSpec,
                    null,
                    progressListener
                ).cache()
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    companion object {
        private const val TAG = "PreCacher"

        /**
         * Max video cache size in bytes.
         */
        private const val MAX_CACHE_SIZE_BYTES = 200L * 1024 * 1024 // 200MB
    }
}