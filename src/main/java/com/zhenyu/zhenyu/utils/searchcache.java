package com.zhenyu.zhenyu.utils;

import androidx.lifecycle.MutableLiveData;

import com.zhenyu.zhenyu.Database.BrowsedNews;
import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.List;

public class searchcache {
    public static searchcache seache;
    private MutableLiveData<List<BrowsedNews>> condata;
    private boolean getresult = false;
    private searchcache(){
        condata = new MutableLiveData<>();
    }
    public static searchcache getInstance(){
        if(seache == null)
            seache = new searchcache();
        return seache;
    }
    public void setCondata(List<BrowsedNews> w){
        condata.postValue(w);
    }
    public MutableLiveData<List<BrowsedNews>> getCondata(){
        return condata;
    }
    public void cleardata(){
        condata = new MutableLiveData<>();
    }
    public void setGetresult(boolean s){
        getresult = s;
    }
    public boolean isGetresult(){
        return getresult;
    }
}
