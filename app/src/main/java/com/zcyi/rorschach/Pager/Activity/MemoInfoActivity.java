package com.zcyi.rorschach.Pager.Activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zcyi.rorschach.Dao.MemoDao;
import com.zcyi.rorschach.DataBase.BaseRoomDatabase;
import com.zcyi.rorschach.DataBase.InstanceDatabase;
import com.zcyi.rorschach.Entity.Memo;
import com.zcyi.rorschach.R;

import java.util.Date;

public class MemoInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Memo_title;
    private EditText Memo_content;

    private TextView Save_time;

    //数据库操作
    BaseRoomDatabase baseRoomDatabase;
    MemoDao memoDao;
    //时间
    String createTime;
    Memo memo;
    AlertDialog.Builder Exit;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    boolean isSaved = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_memo);
        initView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_back:
                Exit.show();
                break;
            case R.id.header_save:
                if (submit()) {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (!(TextUtils.isEmpty(Memo_content.getText().toString().trim()) && TextUtils.isEmpty(Memo_title.getText().toString().trim()))) {
            submit();
        }

        super.onDestroy();
    }

    //保存Memo

    private boolean submit() {
        // validate
        if (TextUtils.isEmpty(Memo_content.getText().toString().trim())) {
            Toast.makeText(this, "内容为空不能保存哦", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(Memo_title.getText().toString().trim())) {
            Toast.makeText(this, "标题为空不能保存哦", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            saveMessage();
            return true;
        }
    }

    private void saveMessage() {
        if (!isSaved) {
            String title = Memo_title.getText().toString().trim();
            String content = Memo_content.getText().toString().trim();
            memoDao.addMemo(new Memo(title, content, createTime, simpleDateFormat.format(new Date(System.currentTimeMillis()))));
        } else {
            memo.setTitle(Memo_title.getText().toString().trim());
            memo.setContent(Memo_content.getText().toString().trim());
            memo.setSaveTime(simpleDateFormat.format(new Date(System.currentTimeMillis())));
            memoDao.updateMemo(memo);
        }
    }

    private void initView() {
        //UI控件
        //基础UI控件
        ImageView header_back = findViewById(R.id.header_back);
        TextView header_title = findViewById(R.id.header_title);
        ImageView header_save = findViewById(R.id.header_save);
        TextView open_time = findViewById(R.id.open_time);
        Memo_title = findViewById(R.id.Memo_title);
        Memo_content = findViewById(R.id.Memo_content);
        Save_time = findViewById(R.id.save_time);

        //获取RoomDatabase实例
        baseRoomDatabase = InstanceDatabase.getInstance(this);
        memoDao = baseRoomDatabase.getMemoDao();

        //设置点击事件
        header_back.setOnClickListener(this);
        header_save.setOnClickListener(this);

        //设置header标题
        Intent intent = getIntent();
        if (intent != null) {
            isSaved = intent.getBooleanExtra("isSaved", false);
            if (!isSaved) {
                String title = intent.getStringExtra("title");
                if (!title.isEmpty()) {
                    header_title.setText(title);
                }
                //设置打开时间
                createTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                open_time.setText(createTime);
            } else {
                header_title.setText("");
                memo = (Memo) intent.getSerializableExtra("memo");
                Memo_title.setText(memo.getTitle());
                Memo_content.setText(memo.getContent());
                open_time.setText(memo.getCreateTime());
                Save_time.setText(memo.getSaveTime());
            }
        }
        Exit = new AlertDialog.Builder(Memo_title.getContext());
        Exit.setTitle("确定要退出吗？").setMessage("退出默认不保存备忘录！")
                .setPositiveButton("确定", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    ShowToast("取消");
                    dialog.cancel();
                });
    }

    public void ShowToast(String info) {
        Toast.makeText(getApplication(), info, Toast.LENGTH_SHORT).show();
    }
}
