package com.zhenyu.zhenyu;


import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhenyu.zhenyu.NewsPages.H_SectionsPagerAdapter;
import com.zhenyu.zhenyu.NewsPages.SectionsPagerAdapter;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    private H_SectionsPagerAdapter sectionsPagerAdapter;
    private ArrayList<Integer> current=new ArrayList<Integer>();
    private ArrayList<Integer>notuse=new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTheme(R.style.Default);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history_main);
        initViewPage();
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
        tabs.setupWithViewPager(viewPager);
    }
}