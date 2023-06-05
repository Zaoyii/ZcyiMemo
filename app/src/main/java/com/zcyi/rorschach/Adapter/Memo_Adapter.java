package com.zcyi.rorschach.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zcyi.rorschach.Dao.MemoDao;
import com.zcyi.rorschach.Entity.Memo;
import com.zcyi.rorschach.Pager.Activity.MemoInfoActivity;
import com.zcyi.rorschach.R;

import java.util.ArrayList;

public class Memo_Adapter extends RecyclerView.Adapter<Memo_Adapter.Memo_ViewHolder> {

    ArrayList<Memo> list;
    Context context;
    boolean isVisibility = false;

    MemoDao memoDao;

    AlertDialog.Builder Delete;

    public Memo_Adapter(Context context, ArrayList<Memo> list, MemoDao memoDao) {
        this.list = list;
        this.context = context;
        this.memoDao = memoDao;

    }

    public ArrayList<Memo> getList() {
        return list;
    }

    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }

    @NonNull
    @Override
    public Memo_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_memo, null);

        return new Memo_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Memo_ViewHolder holder, int position) {

        holder.memo_Title.setText(list.get(position).getTitle());
        holder.memo_Content.setText(list.get(position).getContent());
        holder.memo_saveTime.setText(list.get(position).getSaveTime());

        holder.memo_lin.setOnClickListener(v -> {
            Intent intent = new Intent(context, MemoInfoActivity.class);
            intent.putExtra("memo", list.get(position));
            intent.putExtra("isSaved", true);
            context.startActivity(intent);
        });
        holder.memo_menu.setOnClickListener(v -> {
            showMenu(holder.memo_menu, position);
        });

    }

    private void showMenu(View v, int position) {
        if (Delete == null) {
            Delete = new AlertDialog.Builder(v.getContext());
            Delete.setTitle("确定要删除吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        deleteCurrent(position);
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        dialog.cancel();
                    });
        }

        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.setGravity(Gravity.START);
        popupMenu.getMenuInflater().inflate(R.menu.memo_menu_delete, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.memo_delete) {
                Delete.show();
            }
            return false;
        });
        popupMenu.show();
    }

    public void deleteCurrent(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Memo_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout memo_lin;
        TextView memo_Title;
        TextView memo_Content;
        TextView memo_saveTime;
        ImageView memo_menu;

        public Memo_ViewHolder(@NonNull View itemView) {
            super(itemView);
            memo_lin = itemView.findViewById(R.id.item_memo_lin);
            memo_Title = itemView.findViewById(R.id.memo_title);
            memo_Content = itemView.findViewById(R.id.memo_content);
            memo_saveTime = itemView.findViewById(R.id.memo_save_time);
            memo_menu = itemView.findViewById(R.id.memo_menu);
        }
    }
}
