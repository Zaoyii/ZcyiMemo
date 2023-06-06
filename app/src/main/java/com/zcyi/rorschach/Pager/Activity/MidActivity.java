package com.zcyi.rorschach.Pager.Activity;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zcyi.rorschach.MainActivity;

public class MidActivity extends Activity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("zcyi", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("RingRing", true);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("alarmMe", 1); // 设置启动的模式
        startActivity(intent);
    }

}
