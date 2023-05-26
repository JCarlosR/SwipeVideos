package com.programacionymas.swipevideos.model

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.offline.StreamKey
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist
import java.util.Collections

data class VideoItem(
    var url: String,
    var title: String,
    var firstFrame: String,
    var ready: Boolean = false
) {
    val mediaItem: MediaItem
        get() {
            if (isHls) {
                return MediaItem.Builder()
                    .setUri(url)
                    .setStreamKeys(
                        Collections.singletonList(
                            StreamKey(HlsMasterPlaylist.GROUP_INDEX_VARIANT, 0)
                        )
                    )
                    .build()
            }

            return MediaItem.fromUri(url)
        }

    val isHls: Boolean
        get() = url.endsWith(".m3u8")
}