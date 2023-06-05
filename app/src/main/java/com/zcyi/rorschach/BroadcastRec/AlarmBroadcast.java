package com.zcyi.rorschach.BroadcastRec;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zcyi.rorschach.Pager.Activity.PlayAlarmAty;

public class AlarmBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("闹钟执行了");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // 取消闹钟
        am.cancel(PendingIntent.getBroadcast(context, getResultCode(), new Intent(context, AlarmBroadcast.class), FLAG_IMMUTABLE));

        Intent i = new Intent(context, PlayAlarmAty.class); // 要启动的类
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 设置启动的模式
        context.startActivity(i);
    }

}
