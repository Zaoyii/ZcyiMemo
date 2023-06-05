package com.zcyi.rorschach.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zcyi.rorschach.Entity.Alarm;

import java.util.List;


@Dao
public interface AlarmDao {
    @Query("select * from t_alarm")
    List<Alarm> selectAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] addAlarm(Alarm... alarms);

    @Update
    int updateAlarm(Alarm memo);

    @Delete
    int DeleteAlarm(Alarm memo);

    @Query("Delete from t_alarm")
    int DeleteAllAlarm();
}
