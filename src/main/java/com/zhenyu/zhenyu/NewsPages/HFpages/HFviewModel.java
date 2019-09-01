package com.zhenyu.zhenyu.NewsPages.HFpages;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.BrowsedNews;
import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.List;

public class HFviewModel extends AndroidViewModel {
    private MediatorLiveData<List<BrowsedNews>> mObservableNews;
    private DataRepository repository;

    public HFviewModel(Application application){
        super(application);
        mObservableNews = new MediatorLiveData<>();
        mObservableNews.setValue(null);
        repository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));
    }

    public void setmObservableNews(String cate){
        if(cate.equals("收藏"))
            mObservableNews.addSource(repository.getLikedNews(), new Observer<List<BrowsedNews>>() {
                @Override
                public void onChanged(List<BrowsedNews> browsedNews) {
                    mObservableNews.setValue(browsedNews);
                }
            });
        else if(cate.equals("历史"))
            mObservableNews.addSource(repository.getHistoricalNews(), new Observer<List<BrowsedNews>>() {
                @Override
                public void onChanged(List<BrowsedNews> browsedNews) {
                    mObservableNews.setValue(browsedNews);
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
    public LiveData<List<BrowsedNews>> getNews(){ return mObservableNews; }
}
