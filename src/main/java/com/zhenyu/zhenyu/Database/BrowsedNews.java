package com.zhenyu.zhenyu.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "BrowsedNews")
public class BrowsedNews {
    @PrimaryKey
    @NonNull
    private  String newsid = "1";

    //    private String data;
    @ColumnInfo(name = "image")
    @TypeConverters(StringListConverter.class)
    private List<String> image;

    @ColumnInfo(name = "publishTime")
    private String publishTime = "";

    @ColumnInfo(name = "title")
    private String title = "";

    @ColumnInfo(name = "content")
    private String content = "";

    @ColumnInfo(name = "categories")
    private String categories = "";

    @ColumnInfo(name = "flag")
    private int flag = 0; // Browsed or liked
    // 0 is browsed, 1 is liked

    @TypeConverters({StringMapConverter.class})
    private HashMap<String, Double> keyscore = null;

    @ColumnInfo(name = "stringkeywords")
    private String stringkeywords = "";

    @ColumnInfo(name = "entryTime")
    private long entryTime = 111111;


    @ColumnInfo(name = "publisher")
    private String publisher = "";

    @ColumnInfo(name = "videourl")
    private String videourl;


    public BrowsedNews(String newsid, List<String> image, String publishTime, String title,
                       String content, String categories, HashMap<String, Double> keyscores, String stringkeywords, long entryTime , String publisher, String videourl,int flag){
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
        this.videourl = videourl;
    }

    public BrowsedNews(){
        this.newsid = "1"; this.stringkeywords = "";
        this.title = "";this.content = ""; this.publishTime = "";
        this.categories = ""; this.keyscore = new HashMap<>(); this.flag = 0;
        this.image = new ArrayList<>();
        this.entryTime = 111;
        publisher = "";
        this.videourl = "";
    }

    public BrowsedNews(NewsEntity newsEntity){
        this.newsid = newsEntity.getNewsid();
        this.stringkeywords = newsEntity.getStringkeywords();
        this.title = newsEntity.getTitle();
        this.content = newsEntity.getContent();
        this.publishTime = newsEntity.getPublishTime();
        this.publisher = newsEntity.getPublisher();
        this.categories = newsEntity.getCategories();
        this.keyscore = newsEntity.getKeyscore();
        this.flag = newsEntity.getFlag();
        this.entryTime = new Date().getTime();
        this.videourl = newsEntity.getVideourl();
        this.image = newsEntity.getImage();
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
    public void setVideourl(String videourl){this.videourl = videourl;}

    //    public void setStringkeywords(String stringkeywords){ this.stringkeywords = stringkeywords; }
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
    public String getPublisher(){return  this.publisher;}
    public String getVideourl(){return videourl;}
}
