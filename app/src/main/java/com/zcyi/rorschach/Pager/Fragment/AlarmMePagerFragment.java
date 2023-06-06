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
    Notification notification;
    TextView isnull;


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
            alarmAdapter = new AlarmAdapter(getContext(), (ArrayList<Alarm>) alarms, alarmDao, () -> {
                isnull.setVisibility(View.VISIBLE);
                alarm_recycler.setVisibility(View.GONE);
            });
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
}
