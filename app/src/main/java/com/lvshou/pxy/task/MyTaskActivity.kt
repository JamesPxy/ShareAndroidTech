package com.lvshou.pxy.task

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseActivity
import com.lvshou.pxy.constant.Constant
import com.lvshou.pxy.utils.PreferenceUtils
import kotlinx.android.synthetic.main.content_my_task.*
import loge
import toast
import java.text.SimpleDateFormat
import java.util.*


class MyTaskActivity : BaseActivity(), View.OnClickListener {

    private var TAG = "james"
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private var count = 0
    private var readCount: String by PreferenceUtils(Constant.START_APP_COUNT, "暂无记录，没有数据")
    private val interval = 4 * 60 * 1000L//4分钟执行一次

    private var selectCalendar = Calendar.getInstance()

    private var timer: Timer? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {
        btnStartTask.setOnClickListener(this)
        btnCancelTask.setOnClickListener(this)
        loge(TAG, readCount)
        tvResult.text = readCount

        var selectHour = 8
        var selectMinute = 52
        timePicker.setIs24HourView(true)
        timePicker.hour = selectHour
        timePicker.minute = selectMinute
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            selectHour = hourOfDay
            selectMinute = minute
            tvTime.text = "current select time is: $hourOfDay:$minute"
        }

        datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            // 获取一个日历对象，并初始化为当前选中的时间
            selectCalendar.set(year, monthOfYear, dayOfMonth, selectHour, selectMinute)
            toast(simpleDateFormat.format(selectCalendar.time))
            tvTime.text = "The execute time is: ${simpleDateFormat.format(selectCalendar.time)}"
        }

        btnLock.setOnClickListener {
            timePicker.isEnabled = false
            datePicker.isEnabled = false
            btnStartTask.isEnabled = false
            btnCancelTask.isEnabled = false
            rootView.visibility = View.INVISIBLE
//            btnStartTask.visibility = View.GONE
//            tvTime.visibility = View.INVISIBLE
//            tvResult.visibility = View.INVISIBLE
            toast("锁定成功:${simpleDateFormat.format(selectCalendar.time)}")
        }

    }

    private fun startTimerTask() {
//        val delay = getDelayTime(selectMonth, selectDay)
        //得到毫秒 不用转换
//        var date = Date()
//        var startDate=selectCalendar.time
        val delay = selectCalendar.time.time - Date().time
        if (delay < 0) {
            toast("请先选择正确的定时任务执行时间")
            return
        }
        val task = object : TimerTask() {
            override fun run() {
                startAPP(this@MyTaskActivity, "com.alibaba.android.rimet")
                loge(TAG, "run: start app count=$count")
            }
        }
//        timer.purge()
        timer = Timer(true)
        timer?.schedule(task, delay, interval)

        toast("定时任务开启成功，将于${simpleDateFormat.format(selectCalendar.time)}开始执行")
        tvResult.text = "The task  has start，interval is：$interval ms, delayTime is:$delay"
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
                timer?.cancel()
                toast("定时任务已取消执行")
                tvResult.text = "定时任务已取消执行"
                stopApp(this@MyTaskActivity, "com.alibaba.android.rimet")
            }
        }
    }

    private fun getDelayTime(month: Int, day: Int): Long {

        /* val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
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
         loge(TAG, "格式化执行时间：${df.format(endDate)}")*/
        var endDate = selectCalendar.time
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