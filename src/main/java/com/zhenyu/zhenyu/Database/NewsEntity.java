package com.zhenyu.zhenyu.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "AugustNews")
public class NewsEntity implements Cloneable{
    @PrimaryKey
    @NonNull
    private  String newsid;

    //    private String data;
    @ColumnInfo(name = "image")
    @TypeConverters({StringListConverter.class})
    private List<String> image;

    @ColumnInfo(name = "publishTime")
    private String publishTime = "";

    @ColumnInfo(name = "title")
    private String title = "";

    @ColumnInfo(name = "content")
    private String content = "";

    @ColumnInfo(name = "categories")
    private String categories = "";

    @ColumnInfo(name = "keyscore")
    @TypeConverters({StringMapConverter.class})
    private HashMap<String, Double> keyscore = new HashMap<>();

    @ColumnInfo(name = "stringkeywords")
    private String stringkeywords = "";


    @ColumnInfo(name = "flag")
    private int flag = 0; // display in "homepage or categorical page"
                      // 0 is homepage, 1 is categorical page, 2 is recommendedNews,  3 is search result

    @ColumnInfo(name = "entryTime")
    private long entryTime = 111111;

    @ColumnInfo(name = "publisher")
    private String publisher = "";

    @ColumnInfo(name = "hfflag")
    private int hfflag; // 0 is not clicked , 1 is clicked, 2 is favorate

    @ColumnInfo(name = "viewTime")
    private long viewTime;

    @ColumnInfo(name = "videourl")
    private String videourl;


    public NewsEntity(){
        this.newsid = "1"; this.stringkeywords = "";
        this.title = "";this.content = ""; this.publishTime = "";
        this.categories = ""; this.keyscore = new HashMap<>(); this.flag = 0;
        this.image = new ArrayList<>();
        entryTime = 1111111;
        publisher = "";
        hfflag = 0;
        viewTime = 0;
        videourl = "";
    }

    public NewsEntity(String newsid, List<String> image, String publishTime, String title,
                      String content, String categories, HashMap<String, Double> keyscores, String stringkeywords, long entryTime ,String publisher,String videourl, int flag){
        this.newsid = newsid;
        this.image = image;
        this.publishTime = publishTime;
        this.title = title;
        this.content = content;
        this.categories = categories;
        this.keyscore = keyscores;
        this.flag = flag;
        this.stringkeywords = stringkeywords;
        this.entryTime = entryTime;
        this.publisher = publisher;
        this.viewTime = entryTime;
        this.hfflag = 0;
        this.videourl = videourl;
    }

    public NewsEntity(BrowsedNews browsedNews){
        this.newsid = browsedNews.getNewsid();
        this.image = browsedNews.getImage();
        this.publisher = browsedNews.getPublisher();
        this.title = browsedNews.getTitle();
        this.content = browsedNews.getContent();
        this.flag = browsedNews.getFlag();
        this.categories = browsedNews.getCategories();
        this.keyscore = browsedNews.getKeyscore();
        this.stringkeywords = browsedNews.getStringkeywords();
        this.entryTime = new Date().getTime();
        this.viewTime = new Date().getTime();
        this.videourl = browsedNews.getVideourl();
        this.hfflag = 1;
    }

    public void setPublisher(String publisher){this.publisher = publisher;}
    public void setNewsid(String newsid){ this.newsid = newsid;}
    public void setImage(List<String> image){ this.image = image; }
    public void setPublishTime(String publishTime) { this.publishTime = publishTime; }
    public void setTitle(String title){ this.title = title; }
    public void setContent(String content){ this.content = content; }
    public void setCategories(String categories){ this.categories = categories;}
    public void setKeyscore(HashMap<String, Double> keyscore){ this.keyscore = keyscore; }
    public void setFlag(int flag){ this.flag = flag; }
    public void setStringkeywords(String stringkeywords){ this.stringkeywords = stringkeywords; }
    public void setEntryTime(long entryTime){ this.entryTime = entryTime; }
    public void setHfflag(int hfflag){ this.hfflag = hfflag;}
    public void setViewTime(long viewTime){ this.viewTime = viewTime; }
    public void setVideourl(String videourl){this.videourl = videourl;}

    public String getPublisher(){return  this.publisher;}
    public String getNewsid(){return this.newsid;}
    public List<String> getImage(){return this.image;}
    public String getPublishTime(){return this.publishTime;}
    public String getTitle(){return this.title;}
    public String getContent(){return this.content;}
    public String getCategories(){return this.categories;}
    public HashMap<String, Double> getKeyscore(){return this.keyscore;}
    public int getFlag(){ return flag; }
    public String getStringkeywords() {return this.stringkeywords;}
    public long getEntryTime() {return entryTime; }
    public String getVideourl(){return videourl;}
    public int getHfflag() {
        return hfflag;
    }
    public long getViewTime(){
        return viewTime;
    }
}
