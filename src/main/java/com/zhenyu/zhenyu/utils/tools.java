package com.zhenyu.zhenyu.utils;

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
}
