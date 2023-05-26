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
 * By percentage doesn't make sense since durations are different. We could calculate the bytes
 * equivalent to the first X seconds, but for now precaching 30 KB per video is enough.
 *
 * - How do we prioritize current video loading over precaching?
 * Still need to look about the PriorityTaskManager, and the cancel method, to somehow stop
 * precaching while buffering current video playback.
 *
 * - Do we need to manually check if a video is already cached before precaching it?
 * No. The cache method takes care of it.
 */
class Mp4PreCacher(private val cacheDataSource: CacheDataSource) {

    fun precacheVideo(videoUrl: String) {
        val videoUri = Uri.parse(videoUrl)

        val dataSpec = DataSpec(videoUri, 0, PreCacher.MAX_BYTES_PER_VIDEO)

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
        val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.execute {
            runCatching {
                CacheWriter(
                    cacheDataSource,
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
        private const val TAG = "Mp4PreCacher"
    }
}