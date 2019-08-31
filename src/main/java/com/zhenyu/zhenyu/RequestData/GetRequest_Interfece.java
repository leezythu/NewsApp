package com.zhenyu.zhenyu.RequestData;

import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface GetRequest_Interfece {
    @GET("queryNewsList")
    Call<NetNews> getCall(@QueryMap Map<String,String> param);
    //

    @POST("login")
    Call<String> login(@QueryMap Map<String, String> param);

    @POST("register")
    Call<String> register(@QueryMap Map<String, String> param);

    @POST("logout")
    Call<String> log_out(@QueryMap Map<String, String> param);

    @GET("favorate")
    Call<NewsEntity> pullFavorate();

    @POST("add")
    Call<String> uploadNew(@QueryMap Map<String, String> param);
}
