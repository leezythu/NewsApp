package com.zhenyu.zhenyu.user;

import android.content.ContentUris;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class UserProfile {
    private HashSet<String> dislikedWords;
    private HashMap<String, sField> preferrence;
    private HashMap<String, Double> catePreference;
    private HashSet<String> blockingWords;
    private static UserProfile mInstance;

    private UserProfile(){
        preferrence = new HashMap<>();
        catePreference = new HashMap<>();
        //“首页" 可表示 “首页”中的新闻，以及 所有种类的新闻的和
        String[] catesall = {"首页","科技", "娱乐", "军事","体育","财经","健康","教育","社会", "汽车"};
        for(String va : catesall)
            preferrence.put(va, new sField(va));
        String[] cates = {"科技", "娱乐", "军事","体育","财经","健康","教育","社会", "汽车"};
        for(String va : cates)
            catePreference.put(va, 0.);
        blockingWords = new HashSet<>();
        dislikedWords = new HashSet<>();

    }

    public static UserProfile getInstance(){
        if(mInstance == null)
            mInstance = new UserProfile();
        return mInstance;
    }



    public void addkeys(String category, HashMap<String, Double> ms){
        catePreference.put(category, catePreference.get(category)+1.);
        Objects.requireNonNull(preferrence.get(category)).addKeyword(ms);
    }

    public void addFavorate(String category, HashMap<String, Double> ms){
        catePreference.put(category, catePreference.get(category) + 1.);
        Objects.requireNonNull(preferrence.get(category)).addFavorate(ms);
    }

    public void addlikedall(HashMap<String, Double> ms){
        preferrence.get("首页").addKeyword(ms);
    }

    public String getFromLikedall(){
        return preferrence.get("首页").getUsedKeys();
    }



    public void adddislike(String wd){
        dislikedWords.add(wd);
    }

    public void adddislikewords(String category, HashMap<String, Double> wds){
        sField temp = preferrence.get(category);
        catePreference.put(category, catePreference.get(category) - 1);
        for(Map.Entry<String, Double> entry : wds.entrySet()){
            assert temp != null;
            double dislikeDecayRate = 0.5;
            temp.reduceLiking(entry.getKey(), entry.getValue() * dislikeDecayRate);
        }
    }

    public void addBlockingWords(String word){ blockingWords.add(word); }
    public void addBlockingWords(String[] words){
        blockingWords.addAll(Arrays.asList(words));
    }

    public String getCategoricalKeyWords(String category){
        if(category == null)
            return preferrence.get("首页").getUsedKeys();
        else
            return preferrence.get(category).getUsedKeys();
    }

    public HashSet<String> getDislikedWords(){
        return getDislikedWords();
    }

    public String getCategory(){
        double sums = 0.;
        double rm = 0.;
        double rd = Math.random();
        for(Double db : catePreference.values())
            sums += db;
        if(sums < 0.5)
            return null;
        for(Map.Entry<String, Double> entry : catePreference.entrySet()){
            rm += entry.getValue()/sums;
            if(rm >= rd)
                return entry.getKey();
        }
        return null;
    }

}
