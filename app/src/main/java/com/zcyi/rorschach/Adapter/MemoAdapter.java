package com.zcyi.rorschach.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.zcyi.rorschach.Util.UtilMethod;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.Memo_ViewHolder> {

    ArrayList<Memo> list;
    Context context;
    MemoDao memoDao;
    int deleteId;
    AlertDialog.Builder Delete;
    AlarmAdapter.NullListener nullListener;
    int setImagePosition;

    public MemoAdapter(ArrayList<Memo> list, MemoDao memoDao, AlarmAdapter.NullListener nullListener) {
        this.list = list;
        this.memoDao = memoDao;
        this.nullListener = nullListener;
    }

    public ArrayList<Memo> getList() {
        return list;
    }

    public void setSetImagePosition(int setImagePosition) {
        this.setImagePosition = setImagePosition;
    }

    @NonNull
    @Override
    public Memo_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_memo, null);
        context = parent.getContext();
        return new Memo_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Memo_ViewHolder holder, int position) {

        holder.memo_Title.setText(list.get(position).getTitle());
        holder.memo_Content.setText(list.get(position).getContent());
        holder.memo_saveTime.setText(list.get(position).getSaveTime());
        if (setImagePosition == 1) {
            if (list.get(position).getImage() != null) {
                holder.insert_image_item.setVisibility(View.VISIBLE);
                holder.insert_image_item.setImageURI(Uri.parse("file://" + list.get(position).getImage()));
            } else {
                holder.insert_image_item.setVisibility(View.GONE);
            }
        } else {
            holder.insert_image_item.setVisibility(View.GONE);
        }

        holder.memo_lin.setOnClickListener(v -> {
            if (list.size() != position) {
                Intent intent = new Intent(context, MemoInfoActivity.class);
                intent.putExtra("memo", list.get(position));
                intent.putExtra("isSaved", true);
                context.startActivity(intent);
            } else {
                notifyItemChanged(position);
            }
        });
        holder.memo_menu.setOnClickListener(v -> {
            deleteId = holder.getAdapterPosition();
            showMenu(holder.memo_menu);
        });
    }

    private void showMenu(View v) {
        if (Delete == null) {
            Delete = new AlertDialog.Builder(v.getContext());
            Delete.setTitle("确定要删除吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        deleteCurrent();
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

    public void deleteCurrent() {
        memoDao.DeleteMemo(list.get(deleteId));
        if (list.get(deleteId).getImage() != null) {
            UtilMethod.DeleteFile(list.get(deleteId).getImage());
        }

        list.remove(deleteId);
        notifyItemRemoved(deleteId);
        if (getList().size() == 0) {
            nullListener.setNull();
        }
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
        ImageView insert_image_item;

        public Memo_ViewHolder(@NonNull View itemView) {
            super(itemView);
            memo_lin = itemView.findViewById(R.id.item_memo_lin);
            memo_Title = itemView.findViewById(R.id.memo_title);
            memo_Content = itemView.findViewById(R.id.memo_content);
            memo_saveTime = itemView.findViewById(R.id.memo_save_time);
            memo_menu = itemView.findViewById(R.id.memo_menu);
            insert_image_item = itemView.findViewById(R.id.insert_image_item);
        }
    }

    public interface LayoutListener {
        void whatLayout();
    }
}
