package com.zhenyu.zhenyu.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "AugustNews")
public class NewsEntity {
    @PrimaryKey
    @NonNull
    private  String newsid;

    //    private String data;
    @ColumnInfo(name = "image")
    private String image = "";

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
                      // 0 is homepage, 1 is categorical page, 2 is recommendedNews

    @ColumnInfo(name = "entryTime")
    private long entryTime = 111111;

    @ColumnInfo(name = "publisher")
    private String publisher = "";


    public NewsEntity(){
        this.newsid = "1"; this.stringkeywords = "";
        this.title = "";this.content = ""; this.publishTime = "";
        this.categories = ""; this.keyscore = new HashMap<>(); this.flag = 0;
        this.image = "";
        entryTime = 1111111;
        publisher = "";
    }

    public NewsEntity(String newsid, String image, String publishTime, String title,
                      String content, String categories, HashMap<String, Double> keyscores, String stringkeywords, long entryTime , String publisher, int flag){
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
    }

    public void setPublisher(String publisher){this.publisher = publisher;}
    public void setNewsid(String newsid){ this.newsid = newsid;}
    public void setImage(String image){ this.image = image; }
    public void setPublishTime(String publishTime) { this.publishTime = publishTime; }
    public void setTitle(String title){ this.title = title; }
    public void setContent(String content){ this.content = content; }
    public void setCategories(String categories){ this.categories = categories;}
    public void setKeyscore(HashMap<String, Double> keyscore){ this.keyscore = keyscore; }
    public void setFlag(int flag){ this.flag = flag; }
    public void setStringkeywords(String stringkeywords){ this.stringkeywords = stringkeywords; }
    public void setEntryTime(long entryTime){ this.entryTime = entryTime; }

    public String getPublisher(){return  this.publisher;}
    public String getNewsid(){return this.newsid;}
    public String getImage(){return this.image;}
    public String getPublishTime(){return this.publishTime;}
    public String getTitle(){return this.title;}
    public String getContent(){return this.content;}
    public String getCategories(){return this.categories;}
    public HashMap<String, Double> getKeyscore(){return this.keyscore;}
    public int getFlag(){ return flag; }
    public String getStringkeywords() {return this.stringkeywords;}
    public long getEntryTime() {return entryTime; }
}
