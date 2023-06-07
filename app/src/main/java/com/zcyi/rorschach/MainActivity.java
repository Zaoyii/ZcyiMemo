package com.zcyi.rorschach;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zcyi.rorschach.Dao.AlarmDao;
import com.zcyi.rorschach.DataBase.BaseRoomDatabase;
import com.zcyi.rorschach.DataBase.InstanceDatabase;
import com.zcyi.rorschach.Entity.Alarm;
import com.zcyi.rorschach.Pager.Fragment.AlarmMePagerFragment;
import com.zcyi.rorschach.Pager.Fragment.MemoPagerFragment;
import com.zcyi.rorschach.Util.UtilMethod;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    boolean isExit = false;

    Notification notification;

    private MediaPlayer mMediaPlayer;
    NotificationManager manager;
    private Vibrator vibrator;

    private PowerManager.WakeLock mWakelock;
    private String content;
    BaseRoomDatabase baseRoomDatabase;
    AlarmDao alarmDao;

    MemoPagerFragment memoPagerFragment;
    AlarmMePagerFragment alarmMePagerFragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler mHandler = new Handler(message -> {
        isExit = false;
        return false;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        UtilMethod.changeStatusBarFrontColor(true, this);
        List<Fragment> list = new ArrayList<>();
        memoPagerFragment = new MemoPagerFragment();
        alarmMePagerFragment = new AlarmMePagerFragment();

        list.add(memoPagerFragment);
        list.add(alarmMePagerFragment);
        baseRoomDatabase = InstanceDatabase.getInstance(this);
        alarmDao = baseRoomDatabase.getAlarmDao();

        sharedPreferences = getSharedPreferences("zcyi", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("你猜猜你现在该干嘛?", "李在赣神魔", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
        manager.createNotificationChannel(channel);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), FLAG_IMMUTABLE);
        notification = new NotificationCompat.Builder(this, "zyci")
                .setContentTitle("李在赣神魔")
                .setContentText("你猜猜你现在该干嘛？")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).build();
        sharedPreferences = getSharedPreferences("zcyi", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getInt("ListStyle", -1) == -1) {
            editor.putInt("ListStyle", 1);
            editor.apply();
        }

        BottomNavigationView navigationView = findViewById(R.id.MainBNV);
        viewPager = findViewById(R.id.MainPager);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.memo);
        myViewpagerFragment fragment = new myViewpagerFragment(getSupportFragmentManager(), 0, list);
        viewPager.setAdapter(fragment);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.setSelectedItemId(R.id.memo);
                        break;
                    case 1:
                        navigationView.setSelectedItemId(R.id.alarmClock);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.memo:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.alarmClock:
                viewPager.setCurrentItem(1);
                return true;

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按就退出了~~~", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }

    public void AlarmMe() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.call_of_slience);
        mMediaPlayer.setLooping(true); // 设置是否对播放的音乐进行循环播放
        mMediaPlayer.start();
        baseRoomDatabase = InstanceDatabase.getInstance(this);
        alarmDao = baseRoomDatabase.getAlarmDao();
        List<Alarm> alarms = alarmDao.selectAll();
        for (Alarm a : alarms) {
            if (System.currentTimeMillis() - a.getAlarmTimeMillis() < 1000 * 60) {
                content = a.getAlarmContent();
                a.setState(2);
                alarmDao.updateAlarm(a);
                Notification();
                startVibrator();
                createDialog();
            }
        }
    }

    private void startVibrator() {
        // 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {500, 1000, 500, 1000}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 0);
    }

    private void createDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle("闹钟")
                .setMessage(content)
                .setNegativeButton("关闭", (dialog, whichButton) -> {
                    mMediaPlayer.stop();
                    vibrator.cancel();
                }).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void Notification() {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            manager.notify(1, notification);
        } else {
            Toast.makeText(this, "先把通知权限打开~", Toast.LENGTH_SHORT).show();
            Uri packageURI = Uri.parse("package:" + this.getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            startActivity(intent);
        }
    }

    public static class myViewpagerFragment extends FragmentPagerAdapter {
        List<Fragment> list;

        public myViewpagerFragment(@NonNull FragmentManager fm, int behavior, List<Fragment> list) {
            super(fm, behavior);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    protected void onDestroy() {

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release(); // 释放掉
        }

        super.onDestroy();

    }

    @Override
    protected void onResume() {
        if (sharedPreferences.getBoolean("RingRing", false)) {
            System.out.println("调用AlarmMe----------");
            AlarmMe();
            editor.putBoolean("RingRing", false);
            editor.apply();
        }
        // 唤醒屏幕
        acquireWakeLock();
        super.onResume();

    }

    private void acquireWakeLock() {

        if (mWakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
                    .getCanonicalName());
            mWakelock.acquire();
        }
    }

    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
    }

    @Override
    protected void onPause() {
        // 释放锁屏
        releaseWakeLock();
        super.onPause();

    }
}