package com.zcyi.rorschach.Pager.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.gzuliyujiang.dialog.DialogConfig;
import com.github.gzuliyujiang.dialog.DialogStyle;
import com.github.gzuliyujiang.wheelpicker.DatimePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity;
import com.github.gzuliyujiang.wheelpicker.widget.DatimeWheelLayout;
import com.zcyi.rorschach.Dao.AlarmDao;
import com.zcyi.rorschach.DataBase.BaseRoomDatabase;
import com.zcyi.rorschach.DataBase.InstanceDatabase;
import com.zcyi.rorschach.Entity.Alarm;
import com.zcyi.rorschach.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmMeActivity extends AppCompatActivity implements View.OnClickListener {
    Button timePicker;
    Button submit;
    TextView alarmTime;
    EditText alarmContent;
    Long alarmTimeMillis;

    BaseRoomDatabase baseRoomDatabase;
    AlarmDao alarmDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_me);
        init();
    }

    private void init() {

        timePicker = findViewById(R.id.time_picker);
        alarmContent = findViewById(R.id.Alarm_content);
        submit = findViewById(R.id.submit);
        alarmTime = findViewById(R.id.alarm_time);
        timePicker.setOnClickListener(this);
        submit.setOnClickListener(this);
        TextView header_title = findViewById(R.id.header_title);
        DialogConfig.setDialogStyle(DialogStyle.Three);
        Intent intent = getIntent();
        if (intent != null) {
            header_title.setText(intent.getStringExtra("title"));
        }
        //获取RoomDatabase实例
        baseRoomDatabase = InstanceDatabase.getInstance(this);
        alarmDao = baseRoomDatabase.getAlarmDao();
    }

    public void testTimePicker() {
        DatimePicker picker = new DatimePicker(this);
        final DatimeWheelLayout wheelLayout = picker.getWheelLayout();
        picker.setOnDatimePickedListener((year, month, day, hour, minute, second) -> {
            String text = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            text += wheelLayout.getTimeWheelLayout().isAnteMeridiem() ? " 上午" : " 下午";
            alarmTime.setText(text);
            try {
                System.out.println(dateToStamp(text));
                alarmTimeMillis = dateToStamp(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        });
        wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
        wheelLayout.setTimeMode(TimeMode.HOUR_24_NO_SECOND);
        wheelLayout.setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(10));
        wheelLayout.setDateLabel("年", "月", "日");
        wheelLayout.setTimeLabel("时", "分", "秒");
        wheelLayout.setIndicatorEnabled(true);
        wheelLayout.setIndicatorColor(ContextCompat.getColor(this, R.color.mainColor));
        wheelLayout.setIndicatorSize(getResources().getDisplayMetrics().density * 2);
        wheelLayout.setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        wheelLayout.setSelectedTextColor(ContextCompat.getColor(this, R.color.mainColor));
        wheelLayout.getYearLabelView().setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        wheelLayout.getMonthLabelView().setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        wheelLayout.getDayLabelView().setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        wheelLayout.getHourLabelView().setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        wheelLayout.getMinuteLabelView().setTextColor(ContextCompat.getColor(this, R.color.mainColor));

        picker.show();

    }

    public long dateToStamp(String time) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        assert date != null;
        return date.getTime();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_picker:
                testTimePicker();
                break;
            case R.id.submit:
                if (alarmTime.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "未选择时间!", Toast.LENGTH_SHORT).show();
                } else {
                    if (alarmTimeMillis > System.currentTimeMillis()) {
                        alarmDao.addAlarm(new Alarm(alarmTime.getText().toString(), alarmTimeMillis, alarmContent.getText().toString(), 1));
                    } else {
                        Toast.makeText(getApplicationContext(), "时间选择有误!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}