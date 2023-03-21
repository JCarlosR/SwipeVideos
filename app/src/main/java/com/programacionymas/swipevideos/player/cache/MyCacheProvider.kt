package com.programacionymas.swipevideos.player.cache

import android.content.Context
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

object MyCacheProvider {
    /**
     * The CacheEvictor determines when to delete cached files.
     * This one deletes the the least recently used cached file first, when there is no more space.
     */
    private val cacheEvictor by lazy {
        LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE_BYTES)
    }

    @Volatile private var INSTANCE: Cache? = null

    fun get(context: Context): Cache {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildCache(context).also { INSTANCE = it }
        }
    }

    private fun buildCache(context: Context): Cache {
        val databaseProvider = buildDatabaseProvider(context)
        return SimpleCache(context.cacheDir, cacheEvictor, databaseProvider)
    }

    /**
     * Let ExoPlayer manage its own SQLite tables for caching.
     */
    private fun buildDatabaseProvider(context: Context): DatabaseProvider {
        return ExoDatabaseProvider(context)
    }

    /**
     * Max video cache size in bytes.
     */
    private const val MAX_CACHE_SIZE_BYTES = 200L * 1024 * 1024 // 200MB
}