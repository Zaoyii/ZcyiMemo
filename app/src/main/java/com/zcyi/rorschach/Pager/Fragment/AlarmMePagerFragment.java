package com.zcyi.rorschach.Pager.Fragment;


import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static com.zcyi.rorschach.Util.Constant.TAG;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.zcyi.rorschach.MainActivity;
import com.zcyi.rorschach.R;

import java.util.Objects;


public class AlarmMePagerFragment extends Fragment {
    //主视图
    View v;
    NotificationManager manager;
    Notification notification;
    private static final String ALARM_ACTION = "SAVE_HISTORY_DATA_ACTION";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_alarmclock, container, false);
        v.findViewById(R.id.clock_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmClockTest();
            }
        });

        return v;
    }

    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //定时任务
            //todo
            Log.e("zcyi", "onReceive: Notification");
            Notification();

        }
    };

    public void alarmClockTest() {
        Log.e(TAG, "alarmClockTest: 调用");
        Intent intent = new Intent();
        intent.setAction(ALARM_ACTION);
        IntentFilter intentFilter = new IntentFilter(ALARM_ACTION);
        v.getContext().registerReceiver(alarmReceiver, intentFilter);
        PendingIntent sender;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            sender = PendingIntent.getBroadcast(v.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        } else {
            sender = PendingIntent.getBroadcast(v.getContext(), 0, intent, 0);

        }

        AlarmManager alarm = (AlarmManager) v.getContext().getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000,0, sender);
    }

    public void Notification() {
        manager = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("zyci", "李在赣神魔", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
        manager.createNotificationChannel(channel);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), MainActivity.class), FLAG_IMMUTABLE);
        notification = new NotificationCompat.Builder(getContext(), "zyci")
                .setContentTitle("在干嘛")
                .setContentText("到点了，该吃药了~大朗")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).build();
        if (NotificationManagerCompat.from(getContext()).areNotificationsEnabled()) {
            playNotificationRing(getContext());
            playNotificationVibrate(getContext());
            manager.notify(1, notification);
        } else {
            Toast.makeText(getContext(), "先把通知权限打开~", Toast.LENGTH_SHORT).show();
            Uri packageURI = Uri.parse("package:" + getContext().getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            startActivity(intent);
        }


    }

    private static void playNotificationRing(Context context) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context, uri);
        rt.play();
    }

    /**
     * 手机震动一下
     */
    private static void playNotificationVibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM) //key
                .build();
        long[] times = {0, 2000};
        vibrator.vibrate(VibrationEffect.createWaveform(times, -1));
        Log.e(TAG, "playNotificationVibrate: 执行震动");
    }

}
