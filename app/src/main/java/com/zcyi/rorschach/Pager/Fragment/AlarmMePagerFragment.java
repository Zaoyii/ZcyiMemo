package com.zcyi.rorschach.Pager.Fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zcyi.rorschach.Adapter.AlarmAdapter;
import com.zcyi.rorschach.Dao.AlarmDao;
import com.zcyi.rorschach.DataBase.BaseRoomDatabase;
import com.zcyi.rorschach.DataBase.InstanceDatabase;
import com.zcyi.rorschach.Entity.Alarm;
import com.zcyi.rorschach.Pager.Activity.AlarmMeActivity;
import com.zcyi.rorschach.R;

import java.util.ArrayList;
import java.util.List;


public class AlarmMePagerFragment extends Fragment {
    //主视图
    View v;
    NotificationManager manager;

    TextView isnull;

    Notification notification;

    RecyclerView alarm_recycler;
    AlarmAdapter alarmAdapter;

    //数据库操作
    BaseRoomDatabase baseRoomDatabase;
    AlarmDao alarmDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_alarm_me, container, false);
        init(v);
        return v;
    }

    public void init(View v) {
        v.findViewById(R.id.clock_add).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AlarmMeActivity.class);
            intent.putExtra("title", "添加提醒");
            startActivity(intent);
        });
        isnull = v.findViewById(R.id.Null);
        alarm_recycler = v.findViewById(R.id.alarm_List_recycler);
        baseRoomDatabase = InstanceDatabase.getInstance(getContext());
        alarmDao = baseRoomDatabase.getAlarmDao();

        getAlarmList();
    }

    private void getAlarmList() {
        List<Alarm> alarms = alarmDao.selectAll();
        if (alarms.size() > 0) {
            alarmAdapter = new AlarmAdapter(getContext(), (ArrayList<Alarm>) alarms, alarmDao);
            LinearLayoutManager alarmManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            alarm_recycler.setAdapter(alarmAdapter);
            alarm_recycler.setLayoutManager(alarmManager);
            //隐藏无备忘录提示
            isnull.setVisibility(View.GONE);
            alarm_recycler.setVisibility(View.VISIBLE);

        } else {
            //显示无备忘录提示，隐藏list
            isnull.setVisibility(View.VISIBLE);
            alarm_recycler.setVisibility(View.GONE);
        }
        alarms.size();
    }


    @Override
    public void onResume() {
        super.onResume();
        getAlarmList();
    }


//    public void Notification() {
//        manager = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel channel = new NotificationChannel("zyci", "李在赣神魔", NotificationManager.IMPORTANCE_HIGH);
//        channel.enableLights(true);
//        channel.enableVibration(true);
//
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build();
//        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
//        manager.createNotificationChannel(channel);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), MainActivity.class), FLAG_IMMUTABLE);
//        notification = new NotificationCompat.Builder(getContext(), "zyci")
//                .setContentTitle("在干嘛")
//                .setContentText("到点了，该吃药了~大朗")
//                .setSmallIcon(R.drawable.logo)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true).build();
//        if (NotificationManagerCompat.from(getContext()).areNotificationsEnabled()) {
//            playNotificationRing(getContext());
//            playNotificationVibrate(getContext());
//            manager.notify(1, notification);
//        } else {
//            Toast.makeText(getContext(), "先把通知权限打开~", Toast.LENGTH_SHORT).show();
//            Uri packageURI = Uri.parse("package:" + getContext().getPackageName());
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//            startActivity(intent);
//        }
//    }

//    private static void playNotificationRing(Context context) {
//
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone rt = RingtoneManager.getRingtone(context, uri);
//        rt.play();
//    }
//
//    /**
//     * 手机震动一下
//     */
//    private static void playNotificationVibrate(Context context) {
//        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_ALARM) //key
//                .build();
//        long[] times = {0, 2000};
//        vibrator.vibrate(VibrationEffect.createWaveform(times, -1));
//        Log.e(TAG, "playNotificationVibrate: 执行震动");
//    }
}
