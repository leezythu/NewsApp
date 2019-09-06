package com.zhenyu.zhenyu.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.List;

public class LogController extends ViewModel {
    private static LogController logController;
    private Context context;
    private MutableLiveData<Boolean> online;
    private MutableLiveData<String> loginfo;
    private String username;
    private String password;

    private LogController(Context ctx){
        context = ctx;
        online = new MutableLiveData<>();
        online.setValue(false);
        loginfo = new MutableLiveData<>();
        loginfo.setValue("before login");
        username = "zhenyu";
    }

    public static LogController getInstance(Context x){
        if(logController == null)
            logController = new LogController(x);
        return logController;
    }

    public void mlogin(String loginfo){
        if(loginfo != null) {
            Toast.makeText(context, loginfo, Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(context, "null loginfo", Toast.LENGTH_LONG).show();
    }

    public void mloginSuccessfully(){
        online.setValue(true);
    }

    public void mlogout(){
        online.setValue(false);
        username = "";
        password = "";
    }
    public void setLoginfo(String info) { loginfo.postValue(info);}
    public void setUsername(String username){ this.username = username;}
    public void setPassword(String password){ this.password = password;}

    public MutableLiveData<Boolean> getOnline(){
        return online;
    }
    public MutableLiveData<String> getLoginfo() {return loginfo; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
}
