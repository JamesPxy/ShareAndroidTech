package com.lvshou.pxy

import android.app.Application
import android.content.ComponentCallbacks2
import com.bumptech.glide.Glide
import com.lvshou.pxy.utils.PreferenceUtils
import com.squareup.leakcanary.LeakCanary

/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 15:52
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)
        }
        PreferenceUtils.setContext(applicationContext)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // clear Glide cache
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        // trim memory
        Glide.get(this).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // low memory clear Glide cache
        Glide.get(this).clearMemory()
    }
}