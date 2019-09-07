package com.zhenyu.zhenyu;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.NewsPages.HFpages.H_SectionsPagerAdapter;
import com.zhenyu.zhenyu.NewsPages.searchGadget.S_SectionsPagerAdapter;
import com.zhenyu.zhenyu.NewsPages.searchGadget.Searchpage;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private S_SectionsPagerAdapter sectionsPagerAdapter;
    private ArrayList<Integer> current=new ArrayList<Integer>();
    private ArrayList<Integer>notuse=new ArrayList<Integer>();
    private DataRepository dataRepository;
    private SearchView mSearchView;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTheme(R.style.Default);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        key = bundle.getString("keyword");
        initViewPage();

        initsearch();
        dataRepository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));

//        Button btn1 = findViewById(R.id.button1);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int ss = 0;
//                try{
//                    ss=dataRepository.getLikedNews().getValue().size();
//                }catch (Exception e){
//                    ss = -1;
//                }
//                Toast.makeText(getApplicationContext(), "database size:" + ss, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Button btn2 = findViewById(R.id.button2);
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int ss = 0;
//                try{
//                    ss=dataRepository.getHistoricalNews().getValue().size();
//                }catch (Exception e){
//                    ss = -1;
//                }
//                Toast.makeText(getApplicationContext(), "database size:" + ss, Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    public void initsearch(){
        mSearchView = (SearchView) findViewById(R.id.searchView);
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                key=query;
                Intent intent = new Intent(getApplicationContext(), Search.class);
                Bundle bundle = new Bundle();
                String keyword=query;
                bundle.putString("keyword",keyword);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                if(query.length() > 0) {
                    Toast.makeText(getApplicationContext(), "key==" + key, Toast.LENGTH_LONG).show();
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
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        Toast.makeText(getApplicationContext(), "key==" + key, Toast.LENGTH_LONG).show();
        sectionsPagerAdapter = new S_SectionsPagerAdapter(this, current, notuse, getSupportFragmentManager(),key);
        ViewPager viewPager = findViewById(R.id.h_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.h_tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setupWithViewPager(viewPager);
    }
}