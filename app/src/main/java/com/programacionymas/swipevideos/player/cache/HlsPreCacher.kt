package com.programacionymas.swipevideos.player.cache

import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import java.util.concurrent.Executors

class HlsPreCacher(private val cacheDataSourceFactory: CacheDataSource.Factory) {
    private lateinit var downloader: HlsDownloader

    private fun cancelPreFetch() {
        downloader.cancel()
    }

    fun precacheVideo(mediaItem: MediaItem) {
        val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.execute {
            runCatching {
                downloader = HlsDownloader(mediaItem, cacheDataSourceFactory)

                downloader.download { _, bytesDownloaded, percentDownloaded ->
                    if (bytesDownloaded >= PreCacher.MAX_BYTES_PER_VIDEO) {
                        cancelPreFetch()
                    }
                    Log.d(TAG, "bytesDownloaded: $bytesDownloaded, percentDownloaded: $percentDownloaded")
                }
            }.onFailure {
                Log.e(TAG,"Error on ${mediaItem.mediaMetadata.mediaUri}: $it")
            }
        }
    }

    companion object {
        private const val TAG = "HlsPreCacher"
    }
}