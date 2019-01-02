package com.lvshou.pxy

import android.app.Application
import android.content.ComponentCallbacks2
import com.bumptech.glide.Glide
import com.didichuxing.doraemonkit.DoraemonKit
import com.lvshou.pxy.utils.CrashCatcher
import com.lvshou.pxy.utils.PreferenceUtils
import loge
import com.didichuxing.doraemonkit.kit.IKit


/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 15:52
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this)
//        }
        //初始化 PreferenceUtils
        PreferenceUtils.setContext(applicationContext)

        //永不crash
        if (!BuildConfig.DEBUG) {
            CrashCatcher.install { _, throwable ->
                var error = throwable.stackTrace
                loge("application", error.contentToString())
                loge("application crash", throwable.localizedMessage)
            }
        }

        DoraemonKit.install(this)
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