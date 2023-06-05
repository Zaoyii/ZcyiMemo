package com.zcyi.rorschach.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilMethod {
    //设置状态栏字体颜色
    public static void changeStatusBarFrontColor(boolean isBlack, Activity activity) {

        if (isBlack) {
            //设置状态栏黑色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    public static long dateToStamp(String time) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(time);
        assert date != null;
        return date.getTime();
    }

}
