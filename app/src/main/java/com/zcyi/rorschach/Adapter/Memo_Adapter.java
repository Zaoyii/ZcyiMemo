package com.zcyi.rorschach.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zcyi.rorschach.Entity.Memo;
import com.zcyi.rorschach.Pager.Activity.MemoInfoActivity;
import com.zcyi.rorschach.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Memo_Adapter extends RecyclerView.Adapter<Memo_Adapter.Memo_ViewHolder> {

    ArrayList<Memo> list;
    Context context;
    boolean isVisibility = false;
    Map<Integer, Boolean> checkboxList = new HashMap<>();

    public Memo_Adapter(Context context, ArrayList<Memo> list) {
        this.list = list;
        this.context = context;
    }

    public Map<Integer, Boolean> getCheckboxList() {
        return checkboxList;
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
        checkboxList.put(position, false);
        holder.checkbox_edit.setOnCheckedChangeListener((compoundButton, b) -> {
            checkboxList.put(position, b);
        });
        holder.checkbox_edit.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        holder.memo_lin.setOnClickListener(v -> {
            Intent intent = new Intent(context, MemoInfoActivity.class);
            intent.putExtra("memo", list.get(position));
            intent.putExtra("isSaved", true);
            context.startActivity(intent);
        });

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
        CheckBox checkbox_edit;

        public Memo_ViewHolder(@NonNull View itemView) {
            super(itemView);
            memo_lin = itemView.findViewById(R.id.item_memo_lin);
            memo_Title = itemView.findViewById(R.id.memo_title);
            memo_Content = itemView.findViewById(R.id.memo_content);
            memo_saveTime = itemView.findViewById(R.id.memo_save_time);
            checkbox_edit = itemView.findViewById(R.id.checkbox_edit);
        }
    }
}
