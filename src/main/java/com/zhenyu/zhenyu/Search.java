package com.zhenyu.zhenyu;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.NewsPages.searchGadget.S_SectionsPagerAdapter;
import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.user.UserProfile;
import com.zhenyu.zhenyu.utils.ViewDialog;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private S_SectionsPagerAdapter sectionsPagerAdapter;
    private ArrayList<Integer> current=new ArrayList<Integer>();
    private ArrayList<Integer>notuse=new ArrayList<Integer>();
    private DataRepository dataRepository;
    private SearchView mSearchView;
    private ViewDialog viewDialog;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        UserProfile profile = UserProfile.getInstance();
        if(profile.getThememode().equals("1"))
            setTheme(R.style.ThemeNight);
        else {
            setTheme(R.style.Default);
        }
//        this.setTheme(R.style.Default);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        key = bundle.getString("keyword");

        viewDialog = new ViewDialog(this);

        initViewPage();

        initsearch();
        dataRepository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));


    }
    public void initsearch(){
        mSearchView = (SearchView) findViewById(R.id.searchView);
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "test query", Toast.LENGTH_LONG).show();
                key=query;
                Reception.searchForNews(key, getApplicationContext());

                viewDialog.showDialog();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //...here i'm waiting 5 seconds before hiding the custom dialog
                        //...you can do whenever you want or whenever your work is done

                        Intent intent = new Intent(getApplicationContext(), Search.class);
                        Bundle bundle = new Bundle();
                        String keyword=key;
                        bundle.putString("keyword",keyword);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                        viewDialog.hideDialog();
                    }
                }, 1000);


                if(query.length() > 0) {
                    Toast.makeText(getApplicationContext(), "what==" + key, Toast.LENGTH_LONG).show();
                    mSearchView.setIconified(true);
                    return true;
                }
                else{
                    return false;
                }



            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)){
//                    mListView.setFilterText(newText);
//                }else{
//                    mListView.clearTextFilter();
//                }

                return false;
            }
        });

}
    public void initViewPage() {
        current.add(0);
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
//        ImageLoader.getInstance().init(configuration);
//        Toast.makeText(getApplicationContext(), "key==" + key, Toast.LENGTH_LONG).show();
        sectionsPagerAdapter = new S_SectionsPagerAdapter(this, current, notuse, getSupportFragmentManager(),key);
        ViewPager viewPager = findViewById(R.id.h_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.h_tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setupWithViewPager(viewPager);
    }
}