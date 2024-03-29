package com.zhenyu.zhenyu.Database;

import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import retrofit2.http.QueryMap;

@Dao
public interface BrowsedNewsDao {

    // For history and favorite news
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBrowesedNews(BrowsedNews news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBrowsedNewsAll(List<BrowsedNews> allnews);

    @Query("select * FROM BrowsedNews where flag = 1 order by entryTime desc")
    LiveData<List<BrowsedNews>> getLikedNews();

    @Query("select * FROM BrowsedNews where flag = 0 order by entryTime desc")
    LiveData<List<BrowsedNews>> getHistoryNews();

    @Query("select * FROM browsednews")
    LiveData<List<BrowsedNews>> getall();

    @Query("delete from BrowsedNews where newsid = :goneid")
    void deletenews(String goneid);

    @Query("delete from BrowsedNews")
    void deleteAll();

    @Query("select * FROM BrowsedNews where flag = 3 and stringkeywords = :key1 order by entryTime")
    LiveData<List<BrowsedNews>> getsearchres(String key1);

    @Query("select * FROM BrowsedNews where flag = 3 order by entryTime")
    LiveData<List<BrowsedNews>> getsearchSimple();
}
