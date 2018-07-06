package com.lvshou.pxy.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("james", "onReceive: do in background");
        Toast.makeText(context, "收到定时广播", Toast.LENGTH_SHORT).show();
//        startAPP(context,"com.kufeng.hj.enjoy");
        startAPP(context,"com.alibaba.android.rimet");
    }

    /*
     * 启动一个app
     */
    public void startAPP(Context context,String appPackageName){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(context, "没有安装该应用", Toast.LENGTH_LONG).show();
        }
    }


}


