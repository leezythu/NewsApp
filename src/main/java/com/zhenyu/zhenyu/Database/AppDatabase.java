package com.zhenyu.zhenyu.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zhenyu.zhenyu.RequestData.Reception;

@Database(entities = {NewsEntity.class, BrowsedNews.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();
    private static final String DATABASE_NAME ="news.db";

    public abstract NewsEntityDao getNewsEntityDao();
    public abstract BrowsedNewsDao getBrowsedNewsDao();

    public static AppDatabase getDatabase(final Context context, final AppExecutors executors){
        if(sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors, DATABASE_NAME);
                }
            }
        }
        return sInstance;
    }

    private static AppDatabase buildDatabase(final Context appContext, final AppExecutors executors, final String DATA_BASENAME){
        return Room.databaseBuilder(appContext, AppDatabase.class, DATA_BASENAME)
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                addDelay();
                                AppDatabase database = AppDatabase.getDatabase(appContext, executors);
                                Reception.getReception(appContext);
                                Reception.request(null, null, null, null, 0);
                                Reception.request(null, "娱乐", null, null, 1);
                                Reception.request(null, "科技", null, null, 1);
                                Reception.request(null, "军事", null, null, 1);
                                database.setDatabaseCreated();
                                System.out.println("data Saved!");
                            }
                        });
                    }
                }).build();
    }

    public static void  onDestroy(){
        sInstance = null;
    }
    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }
    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }
    public LiveData<Boolean> getDatabaseCreated(){ return mIsDatabaseCreated; }
}
