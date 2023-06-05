package com.zcyi.rorschach.Pager.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.zcyi.rorschach.Dao.AlarmDao;
import com.zcyi.rorschach.DataBase.BaseRoomDatabase;
import com.zcyi.rorschach.DataBase.InstanceDatabase;
import com.zcyi.rorschach.Entity.Alarm;
import com.zcyi.rorschach.R;

import java.util.List;

public class PlayAlarmAty extends Activity {
    // 音乐播放器
    private MediaPlayer mMediaPlayer;
    private Vibrator vibrator;
    private PowerManager.WakeLock mWakelock;
    private String content;

    BaseRoomDatabase baseRoomDatabase;
    AlarmDao alarmDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // 播放闹钟铃声
        mMediaPlayer = MediaPlayer.create(this, R.raw.call_of_slience); //使用create方式,创建MediaPlayer对象
        mMediaPlayer.setLooping(true); // 设置是否对播放的音乐进行循环播放
        mMediaPlayer.start();
        baseRoomDatabase = InstanceDatabase.getInstance(getApplication());
        alarmDao = baseRoomDatabase.getAlarmDao();
        List<Alarm> alarms = alarmDao.selectAll();
        for (Alarm a : alarms) {
            if (System.currentTimeMillis() - a.getAlarmTimeMillis() < 1000 * 60) {
                content = a.getAlarmContent();
                a.setState(2);
                alarmDao.updateAlarm(a);
            }
        }
        startVibrator();
        createDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        // 释放锁屏
        releaseWakeLock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 唤醒屏幕
        acquireWakeLock();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release(); // 释放掉
    }

    // 唤醒屏幕
    private void acquireWakeLock() {

        if (mWakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
                    .getCanonicalName());
            mWakelock.acquire();
        }
    }

    // 释放锁屏
    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
    }

    // 震动
    private void startVibrator() {
        // 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
                    finish();
                }).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
