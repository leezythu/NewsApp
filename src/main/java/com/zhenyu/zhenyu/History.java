package com.zhenyu.zhenyu;


import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.NewsPages.HFpages.H_SectionsPagerAdapter;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    private H_SectionsPagerAdapter sectionsPagerAdapter;
    private ArrayList<Integer> current=new ArrayList<Integer>();
    private ArrayList<Integer>notuse=new ArrayList<Integer>();
    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTheme(R.style.Default);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history_main);
        initViewPage();

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
    public void initViewPage() {
        current.add(0);
        current.add(1);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        sectionsPagerAdapter = new H_SectionsPagerAdapter(this, current, notuse, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.h_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.h_tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setupWithViewPager(viewPager);
    }
}