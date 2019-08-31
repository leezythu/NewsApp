package com.zhenyu.zhenyu.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BrowsedNewsDao {

    // For history and favorite news
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBrowesedNews(NewsEntity news);

    @Query("select * FROM BrowsedNews")
    LiveData<List<NewsEntity>> getHistoryNews();

    @Query("select * FROM BrowsedNews where flag = 1")
    LiveData<List<NewsEntity>> getLikedNews();
}
