package com.zhenyu.zhenyu.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import retrofit2.http.FieldMap;

public class UserProfile {
    private HashMap<String, Double> lovewords;
    //单领域喜好
    private HashMap<String, sField> preferrence;
    //category喜好
    private HashMap<String, Double> catePreference;
    private HashSet<String> blockingWords;
    private List<String> searchHistoty;
    private String thememode;
    private final double weightdecay;
    private HashSet<String> recentLover;
    private static UserProfile mInstance;

    private UserProfile(){
        preferrence = new HashMap<>();
        catePreference = new HashMap<>();
        //“首页" 可表示 “首页”中的新闻，以及 所有种类的新闻的和
        String[] catesall = {"首页","推荐","科技", "娱乐", "军事","体育","财经","健康","教育","社会", "汽车","文化"};
        for(String va : catesall)
            preferrence.put(va, new sField(va));
        String[] cates = {"科技", "娱乐", "军事","体育","财经","健康","教育","社会", "汽车","文化"};
        for(String va : cates)
            catePreference.put(va, 0.);
        blockingWords = new HashSet<>();
        searchHistoty = new ArrayList<>();
        lovewords = new HashMap<>();
        thememode = "0";
        weightdecay = 0.9;
        recentLover = new HashSet<>();
    }

    public static UserProfile getInstance(){
        if(mInstance == null)
            mInstance = new UserProfile();
        return mInstance;
    }
    public void setThememode(String mode){ this.thememode = mode;}
    public String getThememode(){return this.thememode;}

    public void addsearchHistory(String his){
        searchHistoty.add(his);
    }
    public List<String> getSearchHistoty(){return searchHistoty;}
    public boolean historyContains(String key){ return searchHistoty.contains(key); }


    public void addLoveWord(String words, double sco){
        for(String wd : recentLover) {
            try {
                lovewords.put(wd, lovewords.get(wd) - 1);
            }catch (Exception e){
                lovewords.remove(wd);
            }
        }
        recentLover.clear();
        if(lovewords.containsKey(words)) {
            recentLover.add(words);
            lovewords.put(words, sco + lovewords.get(words)+1);
        }
        else
            lovewords.put(words, sco);
    }

    public Set<String> getLoveWordSet(){
        return lovewords.keySet();
    }

    public String getLoveWord(){
        double sums = 0.;
        double rm = 0.;
        Random w = new Random();
        double rd = (double)w.nextInt(100) / 100.;
        for(Double db : lovewords.values())
            sums += Math.exp(db);
        if(sums < 0.5)
            return null;
        System.out.println("random is :" + rd);
        for(Map.Entry<String, Double> entry : lovewords.entrySet()){
            rm += Math.exp(entry.getValue())/sums;
            System.out.println("calculated sum : " + rm + " " + entry.getKey());
            if(rm >= rd)
                return entry.getKey();
        }
        return null;
    }

    public void setWeightdecay(){
        List<String> removelist = new ArrayList<>();
        for(Map.Entry<String, Double> entry:lovewords.entrySet()){
            double temp = entry.getValue() * weightdecay;
            if(temp < 0.3)
                removelist.add(entry.getKey());
            else
                lovewords.put(entry.getKey(), entry.getValue()*weightdecay);
        }
        for(String w:removelist)
            lovewords.remove(w);
    }

    //用户点击， 关键词添加

    public void addcategoricalword(String category, String word, double sco){
        try {
            preferrence.get(category).addsingleword(word, sco);
        }catch (Exception e){

        }
    }

    //用户收藏， 关键词添加
    public void addFavorate(String category, HashMap<String, Double> ms){
        catePreference.put(category, catePreference.get(category) + 1.);
        Objects.requireNonNull(preferrence.get(category)).addFavorate(ms);
    }


    public String getFromLikedall(){
        return preferrence.get("首页").getUsedKeys();
    }


    //屏蔽 类别+ 关键词添加
    public void adddislikewords(String category, HashMap<String, Double> wds){
        sField temp = preferrence.get(category);
        catePreference.put(category, catePreference.get(category) - 1);
        for(Map.Entry<String, Double> entry : wds.entrySet()){
            assert temp != null;
            double dislikeDecayRate = 0.5;
            temp.reduceLiking(entry.getKey(), entry.getValue() * dislikeDecayRate);
        }
    }

    //屏蔽
    public void addBlockingWords(String word){ blockingWords.add(word); }
    public void addBlockingWords(String[] words){
        blockingWords.addAll(Arrays.asList(words));
    }

    //获得单一类别喜好的关键词
    public String getCategoricalKeyWords(String category){
        if(category == null)
            return preferrence.get("首页").getUsedKeys();
        else
            return preferrence.get(category).getUsedKeys();
    }

    //用户不喜欢的词
    public HashSet<String> getDislikedWords(){
        return getDislikedWords();
    }

    // 获得喜欢的类别- （按概率）随机获取
    public String getCategory(){
        double sums = 0.;
        double rm = 0.;
        Random w = new Random();
        double rd = (double)w.nextInt(100) / 100.;
        for(Double db : catePreference.values())
            sums += Math.exp(db);
        if(sums < 0.5)
            return null;
        System.out.println("random is :" + rd);
        for(Map.Entry<String, Double> entry : catePreference.entrySet()){
            rm += Math.exp(entry.getValue())/sums;
            System.out.println("calculated sum : " + rm + " " + entry.getKey());
            if(rm >= rd)
                return entry.getKey();
        }
        return null;
    }

    //test
    public String seeCategory(){
        return catePreference.toString();
    }
    public HashSet<String> getBlockingWords() { return blockingWords; }

}
