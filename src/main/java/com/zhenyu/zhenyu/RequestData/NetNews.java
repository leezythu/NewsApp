package com.zhenyu.zhenyu.RequestData;

import android.graphics.drawable.shapes.PathShape;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.Database.StringListConverter;
import com.zhenyu.zhenyu.user.UserProfile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetNews {
    //    private String pageSize;
//    private int total;
//    private String data;
    private String currentPage;
    private LinkedList<NewsData> data;

    public static class NewsData{
        private String image;
        private String publishTime;
        private String title;
        private String content;
        private String category;
        private ArrayList<KeyWords> keywords;
        private String newsID;
        private ArrayList<Orgs> organizations;
        private String publisher;

        class KeyWords{
            private double score;
            private String word;
        }
        public class Orgs{
            private String mention;
            private String linkedURL;

            Orgs(String m,String l){
                mention = m;
                linkedURL = l;
            }
        }
    }


    public String getTitle(){
        return String.valueOf(this.data.get(0).keywords.get(0).word);
    }
    public int getNumbereOfData() {return data.size();}

    public NewsEntity toNewsEntity(int index, int flag, HashSet<String> blocks){
        int keywordCnt = 0;
        NewsData temp;
        try {
            temp = this.data.get(index);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Index is out of bound");
            return null;
        }

        HashMap<String, Double> keyscores = new HashMap<>();
        for(NewsData.KeyWords tt: temp.keywords){
            keyscores.put(tt.word, tt.score);
            if(blocks.contains(tt.word))
                return null;
        }
        StringBuilder temps = new StringBuilder();
        for(String v:keyscores.keySet()) {
            temps.append(v);
            temps.append(" ");
        }
        String stringkeywords = "123";
        try{
            stringkeywords = new String(temps.toString().getBytes("GB2312"),StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }


        if(temp.publisher.equals("其他")) {
            try {
                NewsData.Orgs pubOrg = temp.organizations.get(0);
                temp.publisher = pubOrg.mention;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        Gson gson = new Gson();
        List<String> w = new ArrayList<>();
        Pattern pattern = Pattern.compile("(https?://[\\d\\w/.]+)[\\s,\\]]");
        Matcher m = pattern.matcher(temp.image);
        while (m.find())
            w.add(m.group(1));
        return new NewsEntity(temp.newsID, w, temp.publishTime, temp.title, temp.content, temp.category, keyscores, stringkeywords, new Date().getTime(),temp.publisher, flag);
    }

    public List<NewsEntity> toNewsList(int flag){
        int entityNum = data.size();
        List<NewsEntity> res = new ArrayList<>();
        UserProfile userProfile = UserProfile.getInstance();
        HashSet<String> block = userProfile.getBlockingWords();
        for(int i = 0 ; i < entityNum; i++) {
            NewsEntity temp = toNewsEntity(i, flag, block);
            if(temp == null)
                continue;
            res.add(temp);
        }
        return res;
    }
}

