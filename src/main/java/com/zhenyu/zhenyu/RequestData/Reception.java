package com.zhenyu.zhenyu.RequestData;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.AppExecutors;
import com.zhenyu.zhenyu.Database.BrowsedNews;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.Login;
import com.zhenyu.zhenyu.SingleNews;
import com.zhenyu.zhenyu.user.UserProfile;
import com.zhenyu.zhenyu.utils.LogController;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.zhenyu.zhenyu.utils.searchcache;

public class Reception {
    private static Reception mInstance;
    private static AppDatabase appDatabase;
    private static String logbaseurl = "http://10.0.2.2:8000/news/";

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
        Params.put("size","3");
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
            double w = Math.random();
            if(w > 0.5) {
                word = userProfile.getCategoricalKeyWords(resCategory);
                if (word != null)
                    Params.put("words", word);
            }
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
                .baseUrl(logbaseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);

        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", username);
        usrinfo.put("password", passwd);

        Call<LoginEntity> usr = service.login(usrinfo);

        final LogController logController = LogController.getInstance(null);
        usr.enqueue(new Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                if(response.body() != null) {
                    logController.mlogin(response.body().getInfo());
                    if (response.body().getIcode() == 200)
                        logController.mloginSuccessfully();
                    logController.setLoginfo(response.body().getInfo());
                }else{
                    logController.setLoginfo("null login body");
                }
            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    public static void usrRegister(@NonNull String username, @NonNull String passwd){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(logbaseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);

        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("password", passwd);
        usrinfo.put("username", username);

        Call<LoginEntity> usr = service.register(usrinfo);

        final LogController logController = LogController.getInstance(null);
        usr.enqueue(new Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                if(response.body() != null) {
                    logController.mlogin(response.body().getInfo());
                    if (response.body().getIcode() == 200)
                        logController.mloginSuccessfully();
                    //destroy activity
                    logController.setLoginfo(response.body().getInfo());
                }else {
                    logController.setLoginfo("null response");
                }
            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public static void usrLogout(@NonNull String usrname, @NonNull String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(logbaseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", usrname);
        usrinfo.put("password", password);

        final LogController logController = LogController.getInstance(null);
        Call<LoginEntity> usr = service.log_out(usrinfo);
        usr.enqueue(new Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                if(response.body().getIcode() == 200)
                    logController.mlogout();
                logController.setLoginfo(response.body().getInfo());
            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public static void uploadItem(@NonNull String usrname, @NonNull String idx,@NonNull String item) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(logbaseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", usrname);
        usrinfo.put("newsid", idx);
        usrinfo.put("data", item);

        final LogController logController = LogController.getInstance(null);
        Call<LoginEntity> usr = service.uploadNew(usrinfo);
        usr.enqueue(new Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {

            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public static void syncToLocal(@NonNull String username){
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(logbaseurl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", username);
        Call<LoginEntity> usr = service.pullFavorate(usrinfo);
        Log.w("sync", "in sync local");
        usr.enqueue(new Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                if(response.body() != null) {
                    if(response.body().getIcode() == 200) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<String>>() {}.getType();
                        List<String> ww = gson.fromJson(response.body().getInfo(), listType);
                        Gson engson = new Gson();
                        Type entType = new TypeToken<NewsEntity>() {}.getType();
                        for(String temp:ww) {
                            NewsEntity a = engson.fromJson(temp, entType);
                            Log.w("sync", " "+a.getTitle() + a.getFlag());
                            appDatabase.getBrowsedNewsDao().addBrowesedNews(new BrowsedNews(a));
                        }
                        Log.w("sync", "getFavorate Successfully" + ww.size());
                    }else{
                        Log.w("sync", "getFavorate but failed");
                        LogController.getInstance(null).setLoginfo(response.body().getInfo());
                    }
                }else {
                    Log.w("sync", "null favorate");
                    LogController.getInstance(null).setLoginfo("null body in get favorate");
                }
            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public static void cleanall(@NonNull String username){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(logbaseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", username);
        Call<LoginEntity> usr = service.cleanall(usrinfo);
        final LogController logController = LogController.getInstance(null);
        usr.enqueue(new Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                if(response.body() != null) {
                    if(response.body().getIcode() == 200)
                        logController.setLoginfo("cleaned all");
                    else
                        logController.setLoginfo("clean all fails");
                }else {
                    logController.setLoginfo("null object in clean all");
                }
            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public static void removeone(@NonNull String username, @NonNull String item){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(logbaseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> usrinfo = new HashMap<>();
        usrinfo.put("username", username);
        usrinfo.put("newsid", item);
        Call<LoginEntity> usr = service.removeone(usrinfo);
        final LogController logController = LogController.getInstance(null);
        usr.enqueue(new Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                if (response.body() != null) {
                    if (response.body().getIcode() == 200)
                        logController.setLoginfo("removed one");
                    else
                        logController.setLoginfo("clean all fails");
                } else {
                    logController.setLoginfo("null object in removeone");
                }
            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    public static void searchForNews(@NonNull final String key, Context context){

        final Context icon = context;
        Toast.makeText(context, "call search ", Toast.LENGTH_LONG).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api2.newsminer.net/svc/news/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interfece service = retrofit.create(GetRequest_Interfece.class);
        Map<String, String> param = new HashMap<>();
        param.put("words", key);
        param.put("size", "30");
        Call<NetNews> model = service.getCall(param);

        final String tempKey = key;
        model.enqueue(new Callback<NetNews>() {
            @Override
            public void onResponse(Call<NetNews> call, Response<NetNews> response) {
                Toast.makeText(icon, "response", Toast.LENGTH_LONG).show();
                if(response.body() == null) {
                    return;
                }
                else {
                    searchcache ws = searchcache.getInstance();
                    List<BrowsedNews> st = response.body().toBrowsedNewsList(3, tempKey);
                    if(st.size() == 0)
                        ws.cleardata();
                    else
                        ws.setCondata(st);
//                    Toast.makeText(icon, "get news:"+response.body().toBrowsedNewsList(3, tempKey).size(), Toast.LENGTH_SHORT).show();
//                    appDatabase.getBrowsedNewsDao().addBrowsedNewsAll(response.body().toBrowsedNewsList(3, tempKey));
//                    Toast.makeText(icon, "recieve data:"+st.size() + st.get(0).getImage().toString(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<NetNews> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

}
