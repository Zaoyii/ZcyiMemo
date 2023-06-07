package com.zcyi.rorschach.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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


    public static String getPath(Context context, Uri srcUri) {
        String path = context.getCacheDir() + "/" + System.currentTimeMillis() + ".png";//获取本地目录
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);//context的方法获取URI文件输入流
            if (inputStream == null) return "null";
            OutputStream outputStream = Files.newOutputStream(Paths.get(path));
            copyStream(inputStream, outputStream);//调用下面的方法存储
            inputStream.close();
            outputStream.close();
            return path;//成功返回路径
        } catch (Exception e) {
            e.printStackTrace();
            return "null";//失败返回路径null
        }
    }

    private static void copyStream(InputStream input, OutputStream output) {//文件存储
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int n;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ShowToast(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    public static void DeleteFile(String FileName) {
        File file = new File(FileName);
        if (!file.exists()) { // 要删除的文件不存在
            System.out.println("文件" + FileName + "不存在，删除失败！");

        } else { // 要删除的文件存在
            if (file.isFile()) { // 如果目标文件是文件，判断是文件
                System.out.println(file.delete() + "!!!");//删除文件
            }
        }
    }

}
