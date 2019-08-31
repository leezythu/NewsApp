package com.zhenyu.zhenyu.RequestData;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.AppExecutors;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.user.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Reception {
    private static Reception mInstance;
    private static AppDatabase appDatabase;

    public static Reception getReception(Context context){
        if(mInstance == null){
            synchronized (Reception.class){
                if(mInstance == null){
                    mInstance = new Reception(context);
                }
            }
        }
        return mInstance;
    }


    private Reception(Context c){
        appDatabase = AppDatabase.getDatabase(c, new AppExecutors());
    }


    public static void request(@Nullable String word, @Nullable String categories, @Nullable String startDate, @Nullable String endDate, final int flag){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api2.newsminer.net/svc/news/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> Params = new HashMap<String, String>();
        Params.put("size","10");
        if(categories != null)
            Params.put("categories",categories);
        if(word != null)
            Params.put("words",word);
        if(startDate != null)
            Params.put("startDate", startDate);
        if(endDate != null)
            Params.put("endDate", endDate);

        Call<NetNews> model = service.getCall(Params);

        model.enqueue(new Callback<NetNews>() {
            @Override
            public void onResponse(Call<NetNews> call, Response<NetNews> response) {
                if(response.body() == null) { return ;}
                else {
//                    List<NewsEntity> result = response.body().toNewsList(flag);

                    appDatabase.getNewsEntityDao().addNewsAll(response.body().toNewsList(flag));
                }
            }
            @Override
            public void onFailure(Call<NetNews> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    public static void requestRecommended(@Nullable String word, @Nullable String categories, @Nullable String startDate, @Nullable String endDate){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api2.newsminer.net/svc/news/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);

        UserProfile userProfile = UserProfile.getInstance();

        Map<String, String> Params = new HashMap<String, String>();
        Params.put("size","10");
        String resCategory;

        if(categories != null) {
            Params.put("categories", categories);
            resCategory = categories;
        }
        else {
            resCategory = userProfile.getCategory();
            if(resCategory != null)
                Params.put("categories", resCategory);
        }

        if(word != null)
            Params.put("words",word);
        else {
            word = userProfile.getCategoricalKeyWords(resCategory);
            if(word != null)
                Params.put("words", word);
        }

        if(startDate != null)
            Params.put("startDate", startDate);

        if(endDate != null)
            Params.put("endDate", endDate);

        Call<NetNews> model = service.getCall(Params);

        model.enqueue(new Callback<NetNews>() {
            @Override
            public void onResponse(Call<NetNews> call, Response<NetNews> response) {
                if(response.body() == null) {
                    return;
                }
                else {

                    appDatabase.getNewsEntityDao().addNewsAll(response.body().toNewsList(2));
                }
            }
            @Override
            public void onFailure(Call<NetNews> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public static void usrLogin(@NonNull String username, @NonNull String passwd){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("127.0.0.1:8000/news")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);

        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", username);
        usrinfo.put("password", passwd);
        Call<String> usr = service.login(usrinfo);

        usr.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public static void usrRegister(@NonNull String username, @NonNull String passwd){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("127.0.0.1:8000/news")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);

        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", username);
        usrinfo.put("password", passwd);
        Call<String> usr = service.login(usrinfo);

        usr.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public static void usrLogout(@NonNull String usrname){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("127.0.0.1:8000/news")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("usrname", usrname);

        Call<String> usr = service.log_out(usrinfo);
        usr.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }



}
