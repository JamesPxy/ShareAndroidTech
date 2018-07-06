package com.lvshou.pxy.ui.activity

import android.content.Context
import android.util.Log
import android.view.View
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseActivity
import com.lvshou.pxy.constant.Constant
import com.lvshou.pxy.utils.PreferenceUtils
import kotlinx.android.synthetic.main.content_alarm.*
import loge
import toast
import java.util.*
import android.content.ComponentName
import android.app.ActivityManager
import android.support.annotation.RequiresPermission


class StartAPPActivity : BaseActivity(), View.OnClickListener {

    private var TAG = "james"
    private var count = 0
    private var readCount: String by PreferenceUtils(Constant.START_APP_COUNT, "没有数据")


    override fun init() {
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        loge(TAG, readCount)
    }

    private fun startTimerTask() {
        val task = object : TimerTask() {
            override fun run() {
                startAPP(this@StartAPPActivity, "com.alibaba.android.rimet")
                loge(TAG, "run: start app count=$count")
            }
        }
        val timer = Timer()
        val delay: Int = 60 * 60 * 14 * 1000 + 30 * 10 * 1000 //间隔14小时  八点半之后开始
        val interval = 3 * 60 * 1000//3分钟执行一次
        timer.schedule(task, delay.toLong(), interval.toLong())
//        timer.schedule(task, 2000, 60 * 1 * 1000)
    }

    /*
     * 启动一个app
     */
    private fun startAPP(context: Context, appPackageName: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(appPackageName)
            context.startActivity(intent)
            count++
            PreferenceUtils(Constant.START_APP_COUNT, "执行次数：$count")
                    .putPreference(Constant.START_APP_COUNT, "执行次数：$count")
        } catch (e: Exception) {
            toast("打开APP失败：没有安装该应用")
        }
    }

//    https://blog.csdn.net/lyjIT/article/details/52137186
    /* fun stopApp(context: Context, packageName: String) {
         val mActivityManager = context
                 .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
         // 通过调用ActivityManager的getRunningAppServicees()方法获得系统里所有正在运行的进程
         val runServiceList = mActivityManager
                 .getRunningServices(50)
         println(runServiceList.size)
         // ServiceInfo Model类 用来保存所有进程信息
         for (runServiceInfo in runServiceList) {
             val serviceCMP = runServiceInfo.service
             val serviceName = serviceCMP.shortClassName // service 的类名
             val pkgName = serviceCMP.packageName // 包名

             if (pkgName == packageName) {
                 mActivityManager.forceStopPackage(packageName)
 //                mActivityManager.killBackgroundProcesses(packageName)
             }
         }
     }*/


    override fun onClick(v: View) {
        when (v.id) {
            R.id.button1 ->
                startTimerTask()
            R.id.button2 -> {
//                loge(TAG,"os$(android.os.Process.myPid())")
//                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }


    override fun setLayoutId(): Int = R.layout.activity_alarm

    override fun cancelRequest() {
    }
}