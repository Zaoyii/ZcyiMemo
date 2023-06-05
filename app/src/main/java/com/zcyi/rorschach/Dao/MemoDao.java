package com.zcyi.rorschach.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zcyi.rorschach.Entity.Memo;

import java.util.List;


@Dao
public interface MemoDao {
    @Query("select * from t_memo")
    List<Memo> selectAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] addMemo(Memo... memo);

    @Update
    int updateMemo(Memo memo);

    @Delete
    int DeleteMemo(Memo memo);

    @Query("Delete from t_memo")
    int DeleteAllMemo();
}
