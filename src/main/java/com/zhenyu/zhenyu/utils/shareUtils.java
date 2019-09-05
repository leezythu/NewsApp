package com.zhenyu.zhenyu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class shareUtils {

    public static Bitmap textAsBitmap(String text, float textSize) {

        TextPaint textPaint = new TextPaint();

        // textPaint.setARGB(0x31, 0x31, 0x31, 0);
        textPaint.setColor(Color.BLACK);

        textPaint.setTextSize(textSize);

        StaticLayout layout = new StaticLayout(text, textPaint, 450,
                Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,
                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);

        layout.draw(canvas);
        Log.d("textAsBitmap",
                String.format("1:%d %d", layout.getWidth(), layout.getHeight()));
        return bitmap;
    }



    public static Uri getImageUri(Context context, Bitmap inImage) {

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");

        Uri path=context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        if(path != null){
            OutputStream imageout = null;
            try{
                imageout = context.getContentResolver().openOutputStream(path);
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, imageout);
                imageout.close();
            }catch (IOException e){

            }
        }
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return path;
    }

    public static void initImageLoader(Context context) {
        // 获取默认的路径

        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 设置内存图片的宽高
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                // 缓存到磁盘中的图片宽高
                .diskCacheExtraOptions(480, 800, null)
                // .taskExecutor(null)
                // .taskExecutorForCachedImages()
                .threadPoolSize(3)
                // default 线程优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // // default设置在内存中缓存图像的多种尺寸
                //加载同一URL图片时,imageView从小变大时,从内存缓存中加载
                .denyCacheImageMultipleSizesInMemory()
                // 超过设定的缓存大小时,内存缓存的清除机制
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                // 内存的一个大小
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                // default 将图片信息缓存到该路径下
                .diskCache(new UnlimitedDiskCache(cacheDir))
                // default 磁盘缓存的大小
                .diskCacheSize(50 * 1024 * 1024)
                // 磁盘缓存文件的个数
                .diskCacheFileCount(100)
                //磁盘缓存的文件名的命名方式//一般使用默认值 (获取文件名称的hashcode然后转换成字符串)或MD5 new Md5FileNameGenerator()源文件的名称同过md5加密后保存
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // 设置默认的图片加载
                .imageDownloader(
                        new BaseImageDownloader(context)) // default
                // 使用默认的图片解析器
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }
}
