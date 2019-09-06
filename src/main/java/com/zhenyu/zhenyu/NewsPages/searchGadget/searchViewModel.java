package com.zhenyu.zhenyu.NewsPages.searchGadget;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.List;

public class searchViewModel extends AndroidViewModel {
    private MutableLiveData<List<NewsEntity>> mobservable;
    private DataRepository dataRepository;

    public searchViewModel(Application application){
        super(application);
        mobservable = new MutableLiveData<>();
        mobservable.setValue(null);
        dataRepository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));

    }

    public MutableLiveData<List<NewsEntity>> getSearchRes(){return mobservable;}
    public void searchFor(String keys){
        mobservable.setValue(dataRepository.getHomePageNews().getValue());
//        mobservable.setValue(dataRepository.getSearchResult(keys).getValue());
    }
}
