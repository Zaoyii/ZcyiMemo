package com.zcyi.rorschach.Adapter;


import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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

import com.zcyi.rorschach.BroadcastRec.AlarmBroadcast;
import com.zcyi.rorschach.Dao.AlarmDao;
import com.zcyi.rorschach.Entity.Alarm;
import com.zcyi.rorschach.R;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.Alarm_ViewHolder> {

    ArrayList<Alarm> list;
    Context context;
    AlarmDao AlarmDao;

    AlertDialog.Builder Delete;
    AlarmManager am;

    int deleteId;
    public AlarmAdapter(Context context, ArrayList<Alarm> list, AlarmDao AlarmDao) {
        this.list = list;
        this.context = context;
        this.AlarmDao = AlarmDao;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public ArrayList<Alarm> getList() {
        return list;
    }

    @NonNull
    @Override
    public Alarm_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_alarm, null);
        return new Alarm_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Alarm_ViewHolder holder, int position) {
        System.out.println(list.get(position) + "-=-==-=-=--=");
        holder.Alarm_Content.setText(list.get(position).getAlarmContent());
        holder.Alarm_State.setText(list.get(position).getState() == 1 ? "未提醒" : "已提醒");
        holder.Alarm_Time.setText(list.get(position).getAlarmTime());
        holder.Alarm_menu.setOnClickListener(v -> {
            deleteId = holder.getAdapterPosition();
            showMenu(holder.Alarm_menu);
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
        am.cancel(PendingIntent.getBroadcast(context, (int) (list.get(deleteId).getAlarmTimeMillis() / 1000 / 60), new Intent(context, AlarmBroadcast.class), FLAG_IMMUTABLE));
        AlarmDao.DeleteAlarm(list.get(deleteId));
        list.remove(deleteId);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Alarm_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Alarm_lin;
        TextView Alarm_Content;
        TextView Alarm_Time;
        TextView Alarm_State;
        ImageView Alarm_menu;

        public Alarm_ViewHolder(@NonNull View itemView) {
            super(itemView);
            Alarm_lin = itemView.findViewById(R.id.item_alarm_lin);
            Alarm_Content = itemView.findViewById(R.id.alarm_content);
            Alarm_Time = itemView.findViewById(R.id.alarm_time);
            Alarm_State = itemView.findViewById(R.id.alarm_state);
            Alarm_menu = itemView.findViewById(R.id.alarm_menu);
        }
    }
}
