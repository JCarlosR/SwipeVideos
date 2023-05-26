package com.programacionymas.swipevideos.player.cache

import android.util.Log
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.programacionymas.swipevideos.model.VideoItem
import java.util.concurrent.Executors

class HlsPreCacher(private val cacheDataSourceFactory: CacheDataSource.Factory) {
    fun precacheVideo(videoItem: VideoItem) {
        val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.execute {
            runCatching {
                val downloader = HlsDownloader(videoItem.mediaItem, cacheDataSourceFactory)

                downloader.download { _, bytesDownloaded, percentDownloaded ->
                    if (bytesDownloaded >= PreCacher.MAX_BYTES_PER_VIDEO) {
                        Log.d(TAG, "Cancel precache for ${videoItem.shortTitle}")
                        downloader.cancel()
                    }
                    Log.d(TAG, "${videoItem.shortTitle}, kbDownloaded: ${bytesDownloaded/1024}, percentDownloaded: $percentDownloaded")
                }
            }.onFailure {
                Log.e(TAG,"Error on ${videoItem.shortTitle}: $it")
            }
        }
    }

    companion object {
        private const val TAG = "HlsPreCacher"
    }
}