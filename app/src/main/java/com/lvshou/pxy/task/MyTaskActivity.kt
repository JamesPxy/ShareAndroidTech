package com.lvshou.pxy.task

import android.app.ActivityManager
import android.content.Context
import android.view.View
import com.lvshou.pxy.R
import com.lvshou.pxy.R.id.tvResult
import com.lvshou.pxy.base.BaseActivity
import com.lvshou.pxy.constant.Constant
import com.lvshou.pxy.utils.PreferenceUtils
import kotlinx.android.synthetic.main.content_my_task.*
import loge
import toast
import java.util.*
import java.text.SimpleDateFormat


class MyTaskActivity : BaseActivity(), View.OnClickListener {

    private var TAG = "james"
    private var count = 0
    private var readCount: String by PreferenceUtils(Constant.START_APP_COUNT, "暂无记录，没有数据")
    private var selectMonth = 0
    private var selectDay = 0
    private var taskFlag = true
    //    private val executeTime = "14:23:00"
    //    private val interval = 10 * 1000L
    private val executeTime = "08:48:00"
    private val interval = 3 * 60 * 1000L//3分钟执行一次

    private val task = object : TimerTask() {
        override fun run() {
            if (taskFlag) {
                startAPP(this@MyTaskActivity, "com.alibaba.android.rimet")
                loge(TAG, "run: start app count=$count")
            } else {
                stopApp(this@MyTaskActivity, "com.alibaba.android.rimet")
                loge(TAG, "run: stop app count=$count")
            }
            taskFlag = !taskFlag
        }
    }
    private val timer = Timer(true)


    override fun init() {
        btnStartTask.setOnClickListener(this)
        btnCancelTask.setOnClickListener(this)
        loge(TAG, readCount)
        tvResult.text = readCount

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            //月份要加1才等于正常的月份 从0开始计数
            selectMonth = month + 1
            selectDay = dayOfMonth
            tvResult.text = "已选择时间 $year $selectMonth $selectDay "
//            tvResult.text = "已选择时间 $selectDateStr "
        }
    }

    private fun startTimerTask() {
        if (0 == selectMonth || 0 == selectDay) {
            toast("请先选择定时任务执行时间")
            return
        }
        val delay = getDelayTime(selectMonth, selectDay)
        toast("定时任务开启成功，将于$selectMonth:$selectDay 开始执行，间隔时间：$interval ms")
        timer.schedule(task, delay, interval)
        tvResult.text = "定时任务开启成功，将于$selectMonth 月 $selectDay 号，开始执行，间隔时间：$interval ms  delaytime:$delay"
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
    fun stopApp(context: Context, packageName: String) {
        loge(TAG, "start killed")
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
//                 mActivityManager.forceStopPackage(packageName)
                mActivityManager.killBackgroundProcesses(packageName)

                loge(TAG, "has killed")
            }
        }
    }

    fun killProcess(pkgName: String) {
        try {
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String::class.java)
            method.invoke(am, pkgName)
        } catch (e: Exception) {
//            java.lang.reflect.InvocationTargetException
            e.printStackTrace()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnStartTask ->
                startTimerTask()
            R.id.btnCancelTask -> {
//                android.os.Process.killProcess(android.os.Process.myPid())
                timer.cancel()
                toast("定时任务已取消执行")
                tvResult.text = "定时任务已取消执行"
                stopApp(this@MyTaskActivity, "com.alibaba.android.rimet")
            }
        }
    }

    private fun getDelayTime(month: Int, day: Int): Long {

        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var endDate = if (month < 10) {
            if (day < 10) {
                df.parse("2018-0$month-0$day $executeTime")
            } else {
                df.parse("2018-0$month-$day $executeTime")
            }

        } else {
            if (day < 10) {
                df.parse("2018-$month-0$day $executeTime")
            } else {
                df.parse("2018-$month-$day $executeTime")
            }
        }
        loge(TAG, "原始时间：2018-0$month-$day $executeTime")
        loge(TAG, "格式化执行时间：${df.format(endDate)}")
        //得到当前日期
        val date = Date()
        //得到毫秒 不用转换
        val delayTime = endDate.time - date.time
        loge(TAG, "delay time is $delayTime")
        return delayTime
    }


    override fun setLayoutId(): Int = R.layout.activity_my_task

    override fun cancelRequest() {
    }
}