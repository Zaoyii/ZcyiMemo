package com.zcyi.rorschach.Pager.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class MemoPagerFragment extends Fragment implements View.OnClickListener {

    //主视图
    View v;
    //ui控件
    ImageView add_memo;
    TextView isnull;
    RecyclerView memoRecycler;
    LinearLayout memoLin;
    //数据库操作
    BaseRoomDatabase baseRoomDatabase;
    MemoDao memoDao;
    //adapter
    MemoAdapter memoAdapter;
    boolean isEditing;

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

    private void init() {

        //get id
        add_memo = v.findViewById(R.id.memo_add);
        add_memo.setOnClickListener(this);
        memoRecycler = v.findViewById(R.id.memo_List_recycler);
        memoLin = v.findViewById(R.id.memo_list_lin);
        isnull = v.findViewById(R.id.Null);
        //获取roomDataBase实例
        baseRoomDatabase = InstanceDatabase.getInstance(getContext());
        memoDao = baseRoomDatabase.getMemoDao();
        isEditing = false;
        setHasOptionsMenu(true);
        getMemoList();
    }

    private int getMemoList() {
        List<Memo> memos = memoDao.selectAll();
        if (memos.size() > 0) {
            memoAdapter = new MemoAdapter(getContext(), (ArrayList<Memo>) memos,memoDao);
            StaggeredGridLayoutManager memoManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            memoRecycler.setAdapter(memoAdapter);
            memoRecycler.setLayoutManager(memoManager);
            //隐藏无备忘录提示
            isnull.setVisibility(View.GONE);
            memoLin.setVisibility(View.VISIBLE);

        } else {
            //显示无备忘录提示，隐藏list
            isnull.setVisibility(View.VISIBLE);
            memoLin.setVisibility(View.GONE);
        }
        return memos.size();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.memo_add) {//跳转到添加备忘录Activity
            Intent intent = new Intent(getContext(), MemoInfoActivity.class);
            intent.putExtra("title", "添加备忘录");
            intent.putExtra("isSaved", false);
            startActivity(intent);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setGravity(Gravity.START);
        int memo_menu_is_editing = isEditing ? R.menu.memo_menu_not_editing : R.menu.memo_menu_delete;
        popupMenu.getMenuInflater().inflate(memo_menu_is_editing, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.memo_edit:
                    if (isEditing) {
                        memoAdapter.setVisibility(false);
                        isEditing = false;
                    } else {
                        isEditing = true;
                        if (getMemoList() == 0) {
                            Toast.makeText(getContext(), "没有能编辑的备忘录哦~", Toast.LENGTH_SHORT).show();
                        } else {
                            //编辑
                            memoAdapter.setVisibility(true);
                        }
                    }

                    break;
                case R.id.memo_clearALl:
                    if (getMemoList() == 0) {
                        Toast.makeText(getContext(), "没有要清除的备忘录~", Toast.LENGTH_SHORT).show();
                    } else {
                        //删除
                        memoDao.DeleteAllMemo();
                        getMemoList();
                    }
                    break;
            }
            return false;
        });
        popupMenu.show();
    }


}
