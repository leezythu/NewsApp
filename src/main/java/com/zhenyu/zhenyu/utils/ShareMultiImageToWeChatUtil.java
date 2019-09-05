package com.zhenyu.zhenyu.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.zhenyu.zhenyu.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
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
//       File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shareImages", fileName);
//        if (!mFile.getParentFile().exists()) {
//            mFile.getParentFile().mkdirs();
//        }

//        File mFile = new File(Environment.getDownloadCacheDirectory().getAbsolutePath() + File.separator + "shareImages", fileName);
        File mFile = new File(context.getCacheDir().getAbsolutePath() + File.separator + "shareImages", fileName);
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
        textIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public static void shareWechatFriend(Context context,String content, Uri uri ){
        if (isInstallWeChart(context)){
            Intent intent = new Intent();
//            ComponentName cop = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI");
//            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
//            intent.setAction("android.intent.action.SEND_MULTIPLE");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
//                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri);

            intent.putExtra(Intent.EXTRA_TEXT, "hahaha");
//            intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // context.startActivity(intent);
            context.startActivity(Intent.createChooser(intent, "Share"));
        }else{
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareWechatMoment(Context context, String content, Uri uris) {
        if (isInstallWeChart(context)) {
            Intent intent = new Intent();
            //分享精确到微信的页面，朋友圈页面，或者选择好友分享页面
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);

            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uris);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareWechatMoments(Context context, String content, ArrayList<Uri> uris) {
        if (isInstallWeChart(context)) {
            Intent intent = new Intent();
            //分享精确到微信的页面，朋友圈页面，或者选择好友分享页面
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
//            intent.setAction(Intent.ACTION_SEND_MULTIPLE);// 分享多张图片时使用
            intent.setAction(Intent.ACTION_SEND);
            if(uris != null) {
                intent.setType("image/*");
                //添加Uri图片地址--用于添加多张图片
//            ArrayList<Uri> imageUris = new ArrayList<>();
                intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
//                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            }else{
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, content);
            }


            // 微信现不能进行标题同时分享
//            intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }



    public static void shareWechatImages(Context context,String content, ArrayList<Uri> uris){
        if (isInstallWeChart(context)){
            Intent intent = new Intent();
//            ComponentName cop = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI");
//            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
//            intent.setAction("android.intent.action.SEND_MULTIPLE");
            intent.putExtra(Intent.EXTRA_STREAM, uris);
//                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri);

            intent.putExtra(Intent.EXTRA_TEXT, "hahaha");
//            intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // context.startActivity(intent);
            context.startActivity(Intent.createChooser(intent, "Share"));
        }else{
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }
}
