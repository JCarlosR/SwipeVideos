package com.programacionymas.swipevideos.player.cache

import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import java.util.concurrent.Executors

/**
 * Relevant questions and answers:
 *
 * - How much should we cache?
 * By percentage is not convenient since durations are different. We instead cache a size in bytes,
 * specifically 2 MB per video which is enough for downloading the first seconds.
 *
 * - How do we prioritize current video loading over precaching?
 * For this first iteration we are not using any PriorityTaskManager, but we use the cancel method,
 * to stop caching videos that are not adjacent anymore or buffering as current video playback.
 *
 * - Do we need to manually check if a video is already cached before precaching it?
 * No. The cache method takes care of it.
 */
class Mp4PreCacher(private val cacheDataSource: CacheDataSource) {

    private val cacheWriters = arrayListOf<CacheWriter>()

    fun precacheVideo(videoUrl: String) {
        val videoUri = Uri.parse(videoUrl)

        // What to cache
        val dataSpec = DataSpec(videoUri, 0, PreCacher.MAX_BYTES_PER_VIDEO)

        // Download progress
        val progressListener =
            CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                Log.d(TAG, "% $downloadPercentage | requestLength $requestLength | newBytesCached $newBytesCached | video: $videoUri")
            }

        val cacheWriter = CacheWriter(
            cacheDataSource,
            dataSpec,
            null,
            progressListener
        )

        preCacheOnAnotherThread(cacheWriter)
    }

    private fun preCacheOnAnotherThread(cacheWriter: CacheWriter) {
        val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.execute {
            runCatching {
                cacheWriter.cache()
                cacheWriters.add(cacheWriter)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    /**
     * Cancel all downloads.
     */
    fun cancelAllDownloads() {
        cacheWriters.forEach {
            it.cancel()
        }
    }

    companion object {
        private const val TAG = "Mp4PreCacher"
    }
}