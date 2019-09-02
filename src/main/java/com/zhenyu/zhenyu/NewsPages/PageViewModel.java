package com.zhenyu.zhenyu.NewsPages;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.List;

public class PageViewModel extends AndroidViewModel {

    private MediatorLiveData<List<NewsEntity>> mObservableNews;
    private DataRepository repository;
    private int mflag;
    
    public PageViewModel(Application application){
        super(application);
        mObservableNews = new MediatorLiveData<>();
        mObservableNews.setValue(null);
        mflag = 0;
//        repository = ((BasicGadget)application).getDataRepository();
        repository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));
        //link with database
//        LiveData<List<NewsEntity>> news = repository.getNews();
//        LiveData<List<NewsEntity>> news = repository.getCategoricalNews("");
//        mObservableNews.addSource(news, new Observer<List<NewsEntity>>() {
//            @Override
//            public void onChanged(List<NewsEntity> newsEntities) {
//                mObservableNews.setValue(newsEntities);
//            }
//        });
    }


    public void setCategoricalLiveData(String cate){
//        LiveData<List<NewsEntity>> news = repository.getCategoricalNews(cate);
        LiveData<List<NewsEntity>> news;
        if(cate == null || cate.equals("首页")){
            news = repository.getHomePageNews();
            mflag = 0;
        }else if(cate.equals("推荐")){
            news = repository.getRecommendedNews();
            mflag = 2;
        }
        else{
            news = repository.getCategoricalNews(cate);
            mflag = 1;
        }
        mObservableNews.addSource(news, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> newsEntities) {
                mObservableNews.setValue(newsEntities);
            }
        });
    }
    public int getsize() {
        try {
            return mObservableNews.getValue().size();
        }catch (Exception e){
            return 0;
        }
    }
    public LiveData<List<NewsEntity>> getNews(){ return mObservableNews; }
    public int getMflag(){ return mflag; }
}