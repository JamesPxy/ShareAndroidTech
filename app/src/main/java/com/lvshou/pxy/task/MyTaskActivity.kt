package com.lvshou.pxy.task

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowManager
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

    private var TAG = "James"
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private var count = 0
    private var readCount: String by PreferenceUtils(Constant.START_APP_COUNT, "暂无记录，没有数据")
    //private val interval = 10 * 1000L//2分钟执行一次
    private val interval = 20 * 1000L//20s执行一次
    private var selectCalendar = Calendar.getInstance()
    private var timer: Timer? = null
    private var mPackageName = "com.alibaba.android.rimet"

    override fun setLayoutId(): Int = R.layout.activity_my_task

    override fun init() {
        btnStartTask.setOnClickListener(this)
        btnCancelTask.setOnClickListener(this)
        btnLock.setOnClickListener(this)
        loge(TAG, readCount)
        tvResult.text = readCount

        var selectHour = 8
        var selectMinute = 53
        timePicker.setIs24HourView(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = selectHour
            timePicker.minute = selectMinute
        } else {
            timePicker.currentHour = selectHour
            timePicker.currentMinute = selectMinute
        }
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            selectHour = hourOfDay
            selectMinute = minute
            tvTime.text = "current select time is: $hourOfDay:$minute"
        }

        /*   datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
               // 获取一个日历对象，并初始化为当前选中的时间
               selectCalendar.set(year, monthOfYear, dayOfMonth, selectHour, selectMinute)
               toast(simpleDateFormat.format(selectCalendar.time))
               tvTime.text = "The execute time is: ${simpleDateFormat.format(selectCalendar.time)}"
           }*/

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1) { datePicker, year, month, dayOfMonth ->
            loge(TAG, "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth)
            // 获取一个日历对象，并初始化为当前选中的时间
            selectCalendar.set(year, month, dayOfMonth, selectHour, selectMinute)
            toast(simpleDateFormat.format(selectCalendar.time))
            tvTime.text = "The execute time is: ${simpleDateFormat.format(selectCalendar.time)}"
        }
    }

    override fun initImmersionBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


    private fun startTimerTask() {
        //得到毫秒 不用转换
        val delay = selectCalendar.time.time - Date().time
        if (delay < 0) {
            toast("请先选择日期和具体时间!!!!")
            return
        }
        val task = object : TimerTask() {
            override fun run() {
                loge(TAG, "run: start app count=$count")
                when {
                    count < 2 -> startAPP(this@MyTaskActivity, mPackageName)
                    count < 3 -> {
                        startBrowser()
                        loge(TAG, "startBrowser:$count")
                    }
                    count < 4 -> {
                        backToHome()
                        loge(TAG, "backToHome:$count")
                    }
                    /*  count < 4 -> {
                          killProcess(mPackageName)
                          loge(TAG, "killProcess $count")
                      }
                      count < 6 -> {
                          startAPP(this@MyTaskActivity, mPackageName)
                          loge(TAG, "startAPP $count")
                      }
                      count < 8 -> {
                          backToHome()
                          killProcess(mPackageName)
                          startBrowser()
                          loge(TAG, "startBrowser:$count")
                      }*/
                    else -> {
                        loge(TAG, "task has canceled $count")
                        this.cancel()
                        //启动结束任务
                        startFinishTask()
                    }
                }
                count++
            }
        }
        timer = Timer(true)
        timer?.schedule(task, delay, interval)

        toast("--The task  has start，将于${simpleDateFormat.format(selectCalendar.time)}开始执行")
        tvResult.text = "The task  has start，interval is：$interval ms, delayTime is:$delay"
    }

    private fun startFinishTask() {
        count = 0
        selectCalendar.set(Calendar.HOUR_OF_DAY, 18)
        selectCalendar.set(Calendar.MINUTE, 1)
        val delay = selectCalendar.time.time - Date().time
        if (delay < 0) {
            toast("延迟时间有问题，任务不能执行")
            return
        }
        val task = object : TimerTask() {
            override fun run() {
                loge(TAG, "run: start app count=$count")
                when {
                    count < 2 -> startAPP(this@MyTaskActivity, mPackageName)
                    count < 3 -> {
                        startBrowser()
                        loge(TAG, "startBrowser:$count")
                    }
                    count < 4 -> {
                        backToHome()
                        loge(TAG, "backToHome:$count")
                    }
                    else -> {
                        loge(TAG, "task has canceled $count")
                        this.cancel()
                    }
                }
                count++
            }
        }
        timer = Timer(true)
        timer?.schedule(task, delay, interval)
        toast("--The task  has start，将于${simpleDateFormat.format(selectCalendar.time)}开始执行")
        tvResult.text = "The task  has start ${simpleDateFormat.format(selectCalendar.time)} ，interval is：$interval ms, delayTime is:$delay"
    }

    /*
     * 启动一个app
     */
    private fun startAPP(context: Context, appPackageName: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(appPackageName)
            context.startActivity(intent)
            PreferenceUtils(Constant.START_APP_COUNT, "执行次数：$count")
                    .putPreference(Constant.START_APP_COUNT, "执行次数：$count")
        } catch (e: Exception) {
            toast("打开APP失败：没有安装该应用")
        }
    }

    /**
     * 执行锁定操作
     */
    private fun doLockJob() {
        timePicker.isEnabled = false
        datePicker.isEnabled = false
        btnStartTask.isEnabled = false
        btnCancelTask.isEnabled = false
        rootView.visibility = View.INVISIBLE
        toast("锁定成功:${simpleDateFormat.format(selectCalendar.time)}")
    }

    private fun startBrowser() {
        val uri = Uri.parse("http://prototype.sys.hxsapp.net:8088/V3.8.5/#g=1&p=a-9_3确认订单")
//        val uri = Uri.parse("https://cn.bing.com")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun backToHome() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnStartTask ->
                startTimerTask()
//                startAPP(this@MyTaskActivity, mPackageName)
            R.id.btnCancelTask -> {
//                android.os.Process.killProcess(android.os.Process.myPid())
                timer?.cancel()
                tvResult.text = "任务已取消执行"
                killAPP(this@MyTaskActivity, mPackageName)
            }
            R.id.btnLock -> {
                doLockJob()
//                backToHome()
//                startBrowser()
            }
        }
    }

    override fun cancelRequest() {
    }

    //https://blog.csdn.net/lyjIT/article/details/52137186
    @Suppress("DEPRECATION")
    private fun killAPP(context: Context, packageName: String) {
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
            loge(TAG, "kill process  has  excuated")
        } catch (e: Exception) {
//          System.err: java.lang.reflect.InvocationTargetException
            e.printStackTrace()
            loge(TAG, "kill process  exception:${e.message}")
        }
    }

    private fun shutDown() {
        var intent = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}