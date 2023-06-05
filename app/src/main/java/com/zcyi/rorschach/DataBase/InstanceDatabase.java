package com.zcyi.rorschach.DataBase;


import android.content.Context;

import androidx.room.Room;

public class InstanceDatabase {
    public static BaseRoomDatabase baseRoomDatabase;

    public static BaseRoomDatabase getInstance(Context context) {
        if (baseRoomDatabase == null) {
            baseRoomDatabase = Room.databaseBuilder(context, BaseRoomDatabase.class, "zcyi_database.db").allowMainThreadQueries().build();
        }
        return baseRoomDatabase;
    }
}
