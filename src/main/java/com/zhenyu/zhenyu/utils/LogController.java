package com.zhenyu.zhenyu.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zhenyu.zhenyu.Login;
import com.zhenyu.zhenyu.RequestData.Reception;

public class LogController extends ViewModel {
    private static LogController logController;
    private Context context;
    private MutableLiveData<Boolean> online;

    private LogController(Context ctx){
        context = ctx;
        online = new MutableLiveData<>();
        online.setValue(false);
    }

    public static LogController getInstance(Context x){
        if(logController == null)
            logController = new LogController(x);
        return logController;
    }

    public void mlogin(String loginfo){
        Toast.makeText(context, loginfo, Toast.LENGTH_LONG).show();
    }

    public void mloginSuccessfully(){
        online.setValue(true);
    }

    public void mlogout(){
        online.setValue(false);
    }

    public MutableLiveData<Boolean> getOnline(){
        return online;
    }

}
