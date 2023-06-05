package com.zcyi.rorschach.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_alarm")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alarm_id")
    Integer alarmId;
    @ColumnInfo(name = "alarm_time")
    String alarmTime;
    @ColumnInfo(name = "alarm_time_millis")
    Long alarmTimeMillis;
    @ColumnInfo(name = "alarm_content")
    String alarmContent;
    @ColumnInfo(name = "state")
    Integer state;

    public Alarm(String alarmTime, Long alarmTimeMillis, String alarmContent, Integer state) {
        this.alarmTime = alarmTime;
        this.alarmTimeMillis = alarmTimeMillis;
        this.alarmContent = alarmContent;
        this.state = state;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Long getAlarmTimeMillis() {
        return alarmTimeMillis;
    }

    public void setAlarmTimeMillis(Long alarmTimeMillis) {
        this.alarmTimeMillis = alarmTimeMillis;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
