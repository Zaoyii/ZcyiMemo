package com.zcyi.rorschach.DataBase;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zcyi.rorschach.Dao.AlarmDao;
import com.zcyi.rorschach.Dao.MemoDao;
import com.zcyi.rorschach.Entity.Alarm;
import com.zcyi.rorschach.Entity.Memo;


@Database(entities = {Memo.class, Alarm.class}, version = 1, exportSchema = false)
public abstract class BaseRoomDatabase extends RoomDatabase {
    public abstract MemoDao getMemoDao();

    public abstract AlarmDao getAlarmDao();
}
