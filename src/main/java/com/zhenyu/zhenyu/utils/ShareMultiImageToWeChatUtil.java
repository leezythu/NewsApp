package com.zhenyu.zhenyu.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by skyward on 2017/8/25 0025.
 * email:
 */

public class ShareMultiImageToWeChatUtil {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    //保存文件到指定路径
    public static Uri saveImageToGallery(Context context, Bitmap bmp) {
        /**
         * 这段是去掉7.0通过FileProvider的文件访问，方便把图片分享到朋友圈
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        // 首先保存图片

        String fileName = System.currentTimeMillis() + ".jpg";
       File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shareImages", fileName);
        if (!mFile.getParentFile().exists()) {
            mFile.getParentFile().mkdirs();
        }


        try {
            FileOutputStream fos = new FileOutputStream(mFile);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(mFile); // 传递路径


            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        mFile.getAbsolutePath(), fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void shareWithImage(Context context, String kdescription, Uri paths) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        intent.putExtra("Kdescription", kdescription);
        intent.putExtra(Intent.EXTRA_STREAM, paths);
        context.startActivity(intent);
    }

    public static void shareText(Context context, String content){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(textIntent, "分享"));
    }



    /**不实用微信sdk检查是否安装微信
     * @param context
     * @return
     */
    public static boolean isInstallWeChart(Context context){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.tencent.mm", 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

}
