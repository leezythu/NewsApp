package com.zhenyu.zhenyu.RequestData;

import android.graphics.drawable.shapes.PathShape;

import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    public NewsEntity toNewsEntity(int index, int flag){
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
        }
        StringBuilder temps = new StringBuilder();
        for(String v:keyscores.keySet()) {
            temps.append(v);
            temps.append(" ");
        }
        String stringkeywords = temps.toString();

        String publisher = "Unknown";
        String puburl = "http://www.xinhuanet.com/whxw.htm";
        try {
            NewsData.Orgs pubOrg = temp.organizations.get(0);
            publisher = pubOrg.mention;
            puburl = pubOrg.linkedURL;
        }catch (Exception e){
            e.getMessage();
        }
        return new NewsEntity(temp.newsID, temp.image, temp.publishTime, temp.title, temp.content, temp.category, keyscores, stringkeywords, new Date().getTime(),publisher, puburl,flag);
    }

    public List<NewsEntity> toNewsList(int flag){
        int entityNum = data.size();
        List<NewsEntity> res = new ArrayList<>();
        for(int i = 0 ; i < entityNum; i++)
            res.add(toNewsEntity(i, flag));
        return res;
    }
}

