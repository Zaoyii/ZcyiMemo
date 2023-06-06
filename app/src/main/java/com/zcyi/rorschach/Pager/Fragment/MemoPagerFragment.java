package com.zcyi.rorschach.Pager.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zcyi.rorschach.Adapter.MemoAdapter;
import com.zcyi.rorschach.Dao.MemoDao;
import com.zcyi.rorschach.DataBase.BaseRoomDatabase;
import com.zcyi.rorschach.DataBase.InstanceDatabase;
import com.zcyi.rorschach.Entity.Memo;
import com.zcyi.rorschach.Pager.Activity.MemoInfoActivity;
import com.zcyi.rorschach.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoPagerFragment extends Fragment implements View.OnClickListener {

    //主视图
    View v;
    //ui控件
    ImageView add_memo;
    LinearLayout convert;
    TextView isnull;
    RecyclerView memoRecycler;
    LinearLayout memoLin;
    //数据库操作
    BaseRoomDatabase baseRoomDatabase;
    MemoDao memoDao;
    //adapter
    MemoAdapter memoAdapter;
    boolean isEditing;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    StaggeredGridLayoutManager staggeredManager;
    LinearLayoutManager linearManager;
    int listStyle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_memo, container, false);

        init();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMemoList();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sharedPreferences.getInt("ListStyle", -1) != listStyle) {
            editor.putInt("ListStyle", listStyle);
            editor.apply();
        }
    }

    private void init() {

        //get id
        add_memo = v.findViewById(R.id.memo_add);
        convert = v.findViewById(R.id.convert);
        add_memo.setOnClickListener(this);
        convert.setOnClickListener(this);
        memoRecycler = v.findViewById(R.id.memo_List_recycler);
        memoLin = v.findViewById(R.id.memo_list_lin);
        isnull = v.findViewById(R.id.Null);
        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("zcyi", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //获取roomDataBase实例
        baseRoomDatabase = InstanceDatabase.getInstance(getContext());
        memoDao = baseRoomDatabase.getMemoDao();
        isEditing = false;
        staggeredManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        linearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        getMemoList();
        //根据ListStyle改变list展示方式
        listStyle = sharedPreferences.getInt("ListStyle", -1);
        if (listStyle == 1 || listStyle == -1) {
            memoRecycler.setLayoutManager(staggeredManager);
        } else if (listStyle == 2) {
            memoRecycler.setLayoutManager(linearManager);
        }

    }

    private void getMemoList() {
        List<Memo> memos = memoDao.selectAll();
        if (memos.size() > 0) {
            memoAdapter = new MemoAdapter(getContext(), (ArrayList<Memo>) memos, memoDao, () -> {
                isnull.setVisibility(View.VISIBLE);
                memoLin.setVisibility(View.GONE);
            });
            StaggeredGridLayoutManager memoManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            memoRecycler.setAdapter(memoAdapter);

            //隐藏无备忘录提示
            isnull.setVisibility(View.GONE);
            memoLin.setVisibility(View.VISIBLE);

        } else {
            //显示无备忘录提示，隐藏list
            isnull.setVisibility(View.VISIBLE);
            memoLin.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.memo_add:
                Intent intent = new Intent(getContext(), MemoInfoActivity.class);
                intent.putExtra("title", "添加备忘录");
                intent.putExtra("isSaved", false);
                startActivity(intent);
                break;
            case R.id.convert:
                if (listStyle == 1) {
                    listStyle = 2;
                    memoRecycler.setLayoutManager(linearManager);
                } else if (listStyle == 2) {
                    listStyle = 1;
                    memoRecycler.setLayoutManager(staggeredManager);
                }
                break;
        }

    }
}
