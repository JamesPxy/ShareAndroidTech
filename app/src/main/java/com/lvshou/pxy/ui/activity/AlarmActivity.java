package com.lvshou.pxy.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lvshou.pxy.R;
import com.lvshou.pxy.utils.PreferenceUtils;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private AlarmManager manager;
    private PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        context = this;
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);

//        AlarmReceiver alarmReceiver = new AlarmReceiver();
//        IntentFilter filter = new IntentFilter("KingJames");
//        registerReceiver(alarmReceiver, filter);

    }

    /**
     * 1.ELAPSED_REALTIME：以手机开机的时间为基准
     * <p>
     * 2.ELAPSED_REALTIME_WAKEUP：以手机开机的时间为基准，并且可以在休眠时发出广播
     * <p>
     * 3.RTC：以UTC标准时间为基准
     * <p>
     * 4.RTC_WAKEUP：以UTC标准时间为基准，并且可以在休眠时发出广播。这种方式是最常见的形式。
     * <p>
     * 5.POWER_OFF_WAKEUP：关机状态下也能进行提示
     *
     * @param context
     */
    public void startService(Context context) {
        Toast.makeText(this, "开启任务", Toast.LENGTH_SHORT).show();

        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);//获取AlarmManager实例
        int interVal = 6 * 1000;  // 6秒
        long triggerAtTime = System.currentTimeMillis() + interVal;
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("KingJames");
        if (null == pi) {
            pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);//开启提醒
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis() + interVal, interVal, pi);
    }


    public void cancelAlarmManager(Context context) {
        Toast.makeText(this, "已关闭提醒", Toast.LENGTH_SHORT).show();
        manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("KingJames");
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        manager.cancel(pi);
    }

    private int count = 0;

    private void startTimerTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
//                Intent intent = new Intent();
//                intent.setAction("KingJames");
//                sendBroadcast(intent);
                startAPP(context, "com.alibaba.android.rimet");
                Log.e("james", "run: start app count=" + count);
            }
        };
        Timer timer = new Timer();
        int delay = 60 * 60 * 14 * 1000;//间隔14小时
//        int delay = 60 * 60 * 14 * 1000 + 30 * 10 * 1000;//间隔14小时  八点半之后开始
        int interval = 3 * 60 * 1000;//3分钟执行一次
//        timer.schedule(task, delay, interval);
        timer.schedule(task, 2000, 60 * 1 * 1000);
    }

    /*
     * 启动一个app
     */
    private void startAPP(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
            count++;
            PreferenceUtils.Companion.setContext(context);
        } catch (Exception e) {
            Toast.makeText(context, "没有安装该应用", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
//                startService(context);
                startTimerTask();
                break;
            case R.id.button2:
                cancelAlarmManager(context);
                break;
            default:
                break;
        }
    }

}
