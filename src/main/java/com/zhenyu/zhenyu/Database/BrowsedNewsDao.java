package com.zhenyu.zhenyu.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BrowsedNewsDao {

    // For history and favorite news
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBrowesedNews(BrowsedNews news);


    @Query("select * FROM BrowsedNews where flag = 1 order by entryTime desc")
    LiveData<List<BrowsedNews>> getLikedNews();

    @Query("select * FROM BrowsedNews where flag = 0 order by entryTime desc")
    LiveData<List<BrowsedNews>> getHistoryNews();

    @Query("delete from BrowsedNews where newsid = :goneid")
    void deletenews(String goneid);
}
