package com.lentimosystems.swipevideos

import android.app.Application
import android.content.Context

/**
 * Some samples were using the appContext to access the `cacheDir`.
 * If testing shows no issues, we should be ok using any context.
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        private lateinit var appContext: Context

        fun getAppContext(): Context {
            return appContext
        }
    }

}