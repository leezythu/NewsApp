package com.zhenyu.zhenyu;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.BrowsedNews;
import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;
    private final AppDatabase appDatabase;
    private MediatorLiveData<List<NewsEntity>> mObservableNews;
    private MediatorLiveData<List<NewsEntity>> mRecommendedNews;

    public DataRepository(final AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        mObservableNews = new MediatorLiveData<>();

        mObservableNews.addSource(appDatabase.getNewsEntityDao().getHomePageNews(), new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> NewsEntities) {
                if(appDatabase.getDatabaseCreated() != null){
                    mObservableNews.postValue(NewsEntities);
                }
            }
        });

        mRecommendedNews = new MediatorLiveData<>();
        mRecommendedNews.addSource(appDatabase.getNewsEntityDao().getRecommendedNews(), new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> newsEntities) {
                if(appDatabase.getDatabaseCreated() != null)
                    mRecommendedNews.postValue(newsEntities);
            }
        });
    }

    public static DataRepository getInstance(final AppDatabase database){
        if(sInstance == null){
            synchronized (DataRepository.class){
                if(sInstance == null){
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public NewsEntity loadNewsById(final String newsID){
        NewsEntity w = appDatabase.getNewsEntityDao().getNewsByIdSync(newsID);
        if(w != null){
            return w;
        }
        else return new NewsEntity("not","", "", "not find", "this is just a test", "test",null,null,0,null, 0);
    }

    public void addNewsToNewsbase(List<NewsEntity> NewNews){
        appDatabase.getNewsEntityDao().addNewsAll(NewNews);
    }
//    void addNewsToBrowsedNews(NewsEntity newsEntity){
//        appDatabase.getBrowsedNewsDao().addBrowesedNews(new BrowsedNews(newsEntity));
//    }

    void addNewsToBrowsedNews(NewsEntity newsEntity){
        List<BrowsedNews> ww = new ArrayList<>();
        ww.add(new BrowsedNews(newsEntity));
        Log.i("add one news", "to browsed");
        appDatabase.getBrowsedNewsDao().addBrowsedNewsAll(ww);
    }

    public int getDatabaseSize() {
        try {
            return mObservableNews.getValue().size();
        }catch (Exception e){
            return 0;
        }
    }

    public LiveData<List<NewsEntity>> getRecommendedNews(){ return mRecommendedNews;}
    public LiveData<List<NewsEntity>> getHomePageNews() {
        return mObservableNews;
    }
    public LiveData<List<NewsEntity>> getCategoricalNews(String category){ return appDatabase.getNewsEntityDao().getCategoricalNews(category);}
    public LiveData<List<NewsEntity>> getnewsHistorical(){return appDatabase.getNewsEntityDao().getHistorical();}
    public LiveData<List<NewsEntity>> getnewsLiked(){return appDatabase.getNewsEntityDao().getliked();}
    public void addnewsHis(NewsEntity newsEntity){ appDatabase.getNewsEntityDao().addViewed(newsEntity);}

    public LiveData<List<BrowsedNews>> getHistoricalNews(){ return appDatabase.getBrowsedNewsDao().getall(); }
    public LiveData<List<BrowsedNews>> getLikedNews(){ return appDatabase.getBrowsedNewsDao().getLikedNews(); }

}

