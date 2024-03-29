package com.zhenyu.zhenyu.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import retrofit2.http.QueryMap;

@Dao
public interface NewsEntityDao {
    @Query("select * FROM AugustNews")
    LiveData<List<NewsEntity>> getAllNews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNews(NewsEntity newsEntity);

    @Query("select * from AugustNews where newsid = :newsID")
    NewsEntity getNewsByIdSync(String newsID);

    @Query("select * from AugustNews where newsid = :newsID")
    LiveData<List<NewsEntity>> getNewsById(String newsID);

    @Query("select * from AugustNews where flag = 0 order by entryTime desc")
    LiveData<List<NewsEntity>> getHomePageNews();

    @Query("delete from AugustNews where newsid = :goneid")
    void deletenews(String goneid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNewsAll(List<NewsEntity> news);

    @Query("select * FROM AugustNews where categories = :category and flag = 1 order by entryTime desc")
    LiveData<List<NewsEntity>> getCategoricalNews(String category);

    @Query("select * FROM AugustNews where publishTime <= :enddate and publishTime >= :startdate")
    LiveData<List<NewsEntity>> getNewsByTime(String startdate, String enddate);

    @Query("select * FROM AugustNews where stringkeywords LIKE '%'||:keys||'%' order by entryTime desc")
    LiveData<List<NewsEntity>> searchNewsByOneKeyword(String keys);

    @Query("select * FROM augustnews where stringkeywords LIKE :key1")
    LiveData<List<NewsEntity>> searchFormat(String key1);

    @Query("select * FROM AugustNews where stringkeywords LIKE '%'||:key1||'%' OR stringkeywords LIKE '%'||:key2||'%'")
    LiveData<List<NewsEntity>> searchNewsByAnyoneKey(String key1, String key2);


    @Query("select * FROM AugustNews where stringkeywords LIKE '%'||:key1||'%' AND stringkeywords LIKE '%'||:key2||'%'")
    LiveData<List<NewsEntity>> searchNewsByTwoKeyWords(String key1, String key2);

    @Query("delete FROM AugustNews where stringkeywords LIKE '%'||:key1||'%'")
    void shiedNews(String key1);

    //For recommendation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRecommendedNews(NewsEntity newsEntity);

    @Query("select * FROM AugustNews where flag = 2 order by entryTime desc")
    LiveData<List<NewsEntity>> getRecommendedNews();

    @Query("select * FROM AugustNews where hfflag = 1 order by viewTime desc")
    LiveData<List<NewsEntity>> getHistorical();
    @Query("select * FROM augustnews where hfflag = 2 order by viewTime desc")
    LiveData<List<NewsEntity>> getliked();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addViewed(NewsEntity newsEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addViewedAll(List<NewsEntity> newsEntity);

    @Query("delete from AugustNews where stringkeywords LIKE '%'|| :key1 || '%'")
    void removeByKeys(String key1);

    @Query("delete from AugustNews where newsid = :newid")
    void removeById(String newid);
}
