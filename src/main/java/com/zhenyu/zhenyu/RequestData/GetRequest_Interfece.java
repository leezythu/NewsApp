package com.zhenyu.zhenyu.RequestData;

import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.Login;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface GetRequest_Interfece {
    @GET("queryNewsList")
    Call<NetNews> getCall(@QueryMap Map<String,String> param);
    //

    @FormUrlEncoded
    @POST("login/")
    Call<LoginEntity> login(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("register/")
    Call<LoginEntity> register(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("logout/")
    Call<LoginEntity> log_out(@FieldMap Map<String, String> param);

    @GET("favorate/")
    Call<LoginEntity> pullFavorate(@QueryMap Map<String, String> param);

    @FormUrlEncoded
    @POST("add/")
    Call<LoginEntity> uploadNew(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("cleanall/")
    Call<LoginEntity> cleanall(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("removeone/")
    Call<LoginEntity> removeone(@FieldMap Map<String, String> param);
}
