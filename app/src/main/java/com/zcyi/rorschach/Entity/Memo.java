package com.zcyi.rorschach.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "t_memo")
public class Memo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    Integer memoId;
    @ColumnInfo(name = "memoTitle")
    String title;
    @ColumnInfo(name = "memoContent")
    String content;
    @ColumnInfo(name = "memoCreateTime")
    String createTime;
    @ColumnInfo(name = "memoSaveTime")
    String saveTime;

    public Memo(String title, String content, String createTime, String saveTime) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.saveTime = saveTime;
    }

    public Integer getMemoId() {
        return memoId;
    }

    public void setMemoId(Integer memoId) {
        this.memoId = memoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "memoId=" + memoId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", saveTime='" + saveTime + '\'' +
                '}';
    }
}
