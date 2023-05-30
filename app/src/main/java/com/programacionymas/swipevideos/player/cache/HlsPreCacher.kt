package com.programacionymas.swipevideos.player.cache

import android.util.Log
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.programacionymas.swipevideos.model.VideoItem
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors

class HlsPreCacher(
    private val cacheDataSourceProvider: CacheDataSourceProvider
) : StreamPreCacher(cacheDataSourceProvider) {

    private val downloaders by lazy {
        arrayListOf<HlsDownloader>()
    }

    private val cacheDataSourceFactory by lazy {
        cacheDataSourceProvider.cacheDataSourceFactory
    }

    override fun precacheVideo(videoItem: VideoItem) {
        val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.execute {
            runCatching {
                val downloader = HlsDownloader(videoItem.mediaItem, cacheDataSourceFactory)

                downloader.download { _, bytesDownloaded, percentDownloaded ->
                    if (bytesDownloaded >= PreCacher.MAX_BYTES_PER_VIDEO) {
                        Log.d(TAG, "Cancel precache for ${videoItem.shortTitle} because already downloaded > 2 MB")
                        downloader.cancel()
                    } else {
                        Log.d(TAG, "${videoItem.shortTitle}, kbDownloaded: ${bytesDownloaded/1024}, percentDownloaded: $percentDownloaded")
                    }
                }

                downloaders.add(downloader)
            }.onFailure {
                if (it is CancellationException) {
                    Log.d(TAG,"Stopped ${videoItem.shortTitle}")
                } else {
                    Log.e(TAG,"Error on ${videoItem.shortTitle}: $it")
                }
            }
        }
    }

    override fun cancelAllDownloads() {
        downloaders.forEach {
            it.cancel()
        }
    }

    companion object {
        private const val TAG = "HlsPreCacher"
    }
}