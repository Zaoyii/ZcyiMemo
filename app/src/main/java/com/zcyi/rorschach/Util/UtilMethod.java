package com.zcyi.rorschach.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    private static void copyStream(InputStream input, OutputStream output) {//文件存储
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPath(Context context, Uri srcUri) {
        String path = context.getCacheDir() + "/" + System.currentTimeMillis() + ".png";//获取本地目录
        File file = new File(path);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);//context的方法获取URI文件输入流
            if (inputStream == null) return "null";
            OutputStream outputStream = Files.newOutputStream(Paths.get(path));
            copyStream(inputStream, outputStream);//调用下面的方法存储
            inputStream.close();
            outputStream.close();
            System.out.println("-=-=-=-=-=--=存储成功");
            return path;//成功返回路径
        } catch (Exception e) {
            e.printStackTrace();
            return "null";//失败返回路径null
        }
    }

    private void code(Context context, ImageView imageView) {
        //将ImageView中的图片转换成Bitmap
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
//将Bitmap 转换成二进制，写入本地

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        File dir = new File("file://" + context.getCacheDir() + "/");
        if (!dir.isFile()) {
            dir.mkdir();
        }
        //makeCheckCode()  是随机的字符串当然你也可以写别的看项目需要
        File file = new File(dir, System.currentTimeMillis() + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray, 0, byteArray.length);
            fos.flush();
            //用广播通知相册进行更新相册
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
