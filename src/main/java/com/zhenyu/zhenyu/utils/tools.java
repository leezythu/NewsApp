package com.zhenyu.zhenyu.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhenyu.zhenyu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tools {
    private static int keycnt = 0;
    private static Map<String,String> keymap = new HashMap<>();
    private static Map<String,String> valuekeymap = new HashMap<>();
    public static String addkeyone(String w){
        String value = "xx" + keycnt + "xx";
        keycnt += 1;
        keymap.put(value,w);
        return value;
    }

    public static String[] addkeysome(String[] w){
        String[] res = new String[w.length];
        for(int i = 0 ; i < w.length; i++){
            String value = "xx" + keycnt + "xx";
            keycnt += 1;
            keymap.put(value,w[i]);
            res[i] = value;
        }
        return res;
    }
    public static List<String> addkeysome(List<String> w){
        List<String> res = new ArrayList<>();
        for(String s:w){
            String value = "xx" + keycnt + "xx";
            keycnt += 1;
            res.add(value);
            keymap.put(value,s);
        }
        return res;
    }
    public static String getchinese(String mk){
        if(keymap.containsKey(mk))
            return keymap.get(mk);
        else
            return "";
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }


    public static DisplayImageOptions initOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.drawable.ic_launcher_foreground)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher_foreground)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher_foreground)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
//                .considerExifParams(true)
//                // 设置图片以如何的编码方式显示
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                // 设置图片的解码类型//
//                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码配置
                // .decodingOptions(options)
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                // 设置图片在下载前是否重置，复位
                .resetViewBeforeLoading(true)
                // 是否设置为圆角，弧度为多少
                .displayer(new RoundedBitmapDisplayer(20))
                // 是否图片加载好后渐入的动画时间
                .displayer(new FadeInBitmapDisplayer(100))
                // 构建完成
                .build();
        return options;
    }
}
