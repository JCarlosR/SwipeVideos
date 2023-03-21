package com.programacionymas.swipevideos.player.cache

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * Questions to resolve:
 * - How much should we cache? Whether percentage wise or fixed initial seconds
 * - How do we prioritize current video loading over precaching?
 * - Do we need to manually check if a video is already cached before precaching it?
 */
class PreCacher(private val context: Context) {

    fun precacheVideo(videoUrl: String) {
        val videoUri = Uri.parse(videoUrl)
        val dataSpec = DataSpec(videoUri)

        val progressListener =
            CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                Log.d(TAG, "download % $downloadPercentage | newBytesCached $newBytesCached | video: $videoUri")
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
                    MyCacheDataSourceProvider(context).getDataSource(),
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
    }
}