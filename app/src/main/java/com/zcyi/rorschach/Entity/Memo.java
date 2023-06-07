package com.zcyi.rorschach.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "t_memo")
public class Memo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    Integer memoId;
    @ColumnInfo(name = "memo_title")
    String title;
    @ColumnInfo(name = "memo_content")
    String content;
    @ColumnInfo(name = "memo_image")
    String image;
    @ColumnInfo(name = "memo_createTime")
    String createTime;
    @ColumnInfo(name = "memo_save_time")
    String saveTime;

    public Memo() {
    }
    @Ignore
    public Memo(String title, String content, String createTime, String saveTime) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.saveTime = saveTime;
    }
    @Ignore
    public Memo(String title, String content, String image, String createTime, String saveTime) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.createTime = createTime;
        this.saveTime = saveTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
