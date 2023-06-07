package com.zcyi.rorschach.Pager.Activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gzuliyujiang.imagepicker.ActivityBuilder;
import com.github.gzuliyujiang.imagepicker.CropImageView;
import com.github.gzuliyujiang.imagepicker.ImagePicker;
import com.github.gzuliyujiang.imagepicker.PickCallback;
import com.zcyi.rorschach.Dao.MemoDao;
import com.zcyi.rorschach.DataBase.BaseRoomDatabase;
import com.zcyi.rorschach.DataBase.InstanceDatabase;
import com.zcyi.rorschach.Entity.Memo;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.Constant;
import com.zcyi.rorschach.Util.UtilMethod;

import java.util.Date;
import java.util.List;

public class MemoInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Memo_title;
    private EditText Memo_content;
    private ImageView imagePicker;
    private ImageView imageInsert;

    private String oldTitle;
    private String oldContent;
    //数据库操作
    BaseRoomDatabase baseRoomDatabase;
    MemoDao memoDao;
    //时间
    String createTime;
    Memo memo;
    AlertDialog.Builder Exit;
    Uri imgUri;
    String imgPath;
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
                listenBack();
                break;
            case R.id.header_save:
                if (submit()) {
                    finish();
                }
                break;
            case R.id.pick_image:
                showPickMenu(imagePicker);
                break;
        }
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
        Log.e(Constant.TAG, "saveMessage: " + isSaved);
        if (!isSaved) {
            String title = Memo_title.getText().toString().trim();
            String content = Memo_content.getText().toString().trim();
            memoDao.addMemo(new Memo(title, content, imgPath, createTime, simpleDateFormat.format(new Date(System.currentTimeMillis()))));
            Log.e(Constant.TAG, "saveMessage: 调用add");
        } else {
            if (!(oldTitle.equals(Memo_title.getText().toString().trim()) && oldContent.equals(Memo_content.getText().toString().trim()))) {
                memo.setTitle(Memo_title.getText().toString().trim());
                memo.setContent(Memo_content.getText().toString().trim());
                memo.setSaveTime(simpleDateFormat.format(new Date(System.currentTimeMillis())));
                memo.setImage(imgPath);
                System.out.println(imgPath+"-=----=-=--=");
                memoDao.updateMemo(memo);
                System.out.println(memo.getMemoId() + "--==--=-===--=");
                List<Memo> memos = memoDao.selectAll();
                System.out.println(memos + "_+_+-=-=-=--=");
                Log.e(Constant.TAG, "saveMessage: 调用update");
            } else {
                Log.e(Constant.TAG, "saveMessage：内容未发生改变，不调用update");
            }
        }
    }

    private void initView() {
        //基础UI控件
        UtilMethod.changeStatusBarFrontColor(true, this);
        ImageView header_back = findViewById(R.id.header_back);
        TextView header_title = findViewById(R.id.header_title);
        ImageView header_save = findViewById(R.id.header_save);
        TextView open_time = findViewById(R.id.open_time);
        Memo_title = findViewById(R.id.Memo_title);
        Memo_content = findViewById(R.id.Memo_content);
        TextView save_time = findViewById(R.id.save_time);
        imagePicker = findViewById(R.id.pick_image);
        imageInsert = findViewById(R.id.insert_image);

        //获取RoomDatabase实例
        baseRoomDatabase = InstanceDatabase.getInstance(this);
        memoDao = baseRoomDatabase.getMemoDao();

        //设置点击事件
        header_back.setOnClickListener(this);
        header_save.setOnClickListener(this);
        imagePicker.setOnClickListener(this);
        imageInsert.setOnClickListener(this);
        oldContent = "";
        oldTitle = "";
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
                System.out.println(memo + "--==--=-===--=");
                Memo_title.setText(memo.getTitle());
                Memo_content.setText(memo.getContent());
                open_time.setText(memo.getCreateTime());
                save_time.setText(memo.getSaveTime());
                oldTitle = memo.getTitle();
                oldContent = memo.getContent();
                System.out.println("--------file://" + memo.getImage());
                imageInsert.setImageURI(Uri.parse("file://" + memo.getImage()));
                imgUri = Uri.parse(memo.getImage());


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            listenBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void listenBack() {
        if (!(oldTitle.equals(Memo_title.getText().toString().trim()) && oldContent.equals(Memo_content.getText().toString().trim()))) {
            Exit.show();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePicker.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ImagePicker.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @SuppressLint("NonConstantResourceId")
    private void showPickMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getApplication(), v);
        popupMenu.setGravity(Gravity.START);
        popupMenu.getMenuInflater().inflate(R.menu.memo_menu_pick, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.memo_gallery:
                    onGallery(v);
                    break;
                case R.id.memo_camera:
                    onCamera(v);
                    break;

            }
            return false;
        });
        popupMenu.show();
    }

    public void onCamera(View view) {
        ImagePicker.getInstance().startCamera(this, true, new PickCallback() {
            @Override
            public void onPermissionDenied(String[] permissions, String message) {

                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cropConfig(ActivityBuilder builder) {
                builder.setMultiTouchEnabled(true)
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH);
            }

            @Override
            public void onCropImage(@Nullable Uri imageUri) {
                System.out.println(getApplication().getCacheDir() + "-=-=---=---=-");
                System.out.println(imageUri + "-=-=---=---=-");
                Toast.makeText(getApplication(), String.valueOf(imageUri), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onGallery(View view) {
        ImagePicker.getInstance().startGallery(this, false, new PickCallback() {
            @Override
            public void onPermissionDenied(String[] permissions, String message) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPickImage(@Nullable Uri imageUri) {
                Toast.makeText(getApplication(), String.valueOf(imageUri), Toast.LENGTH_SHORT).show();
                imgUri = imageUri;
                imageInsert.setImageURI(imgUri);
                System.out.println();
                imgPath = UtilMethod.getPath(getApplication(), imgUri);
            }
        });
    }

}
