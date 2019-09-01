package com.zhenyu.zhenyu;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.AppExecutors;
import com.zhenyu.zhenyu.NewsPages.SectionsPagerAdapter;
import com.zhenyu.zhenyu.RequestData.Reception;


import androidx.drawerlayout.widget.DrawerLayout;



import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private ArrayList<Integer>current=new ArrayList<Integer>();
    private ArrayList<Integer>notuse=new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTheme(R.style.Default);
        setContentView(R.layout.activity_main);
        AppDatabase.getDatabase(getApplicationContext(), new AppExecutors());
        DataRepository.getInstance(AppDatabase.getDatabase(getApplicationContext(), null));
        Reception.getReception(getApplication());
        initDrawer();
        initTabs();
        initViewPage();
        initLogin();
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();
        current=bundle.getIntegerArrayList("current_tabs");
        notuse=bundle.getIntegerArrayList("notuse_tabs");
        Toast.makeText(getApplicationContext(), current.toString(),Toast.LENGTH_LONG).show();
        sectionsPagerAdapter.set_cur(current);
        sectionsPagerAdapter.set_notuse(notuse);
        sectionsPagerAdapter.notifyDataSetChanged();
        initViewPage();
    }


    public void initTabs(){
        current.add(0);
        current.add(1);
        current.add(2);
        current.add(3);
        notuse.add(4);
        notuse.add(5);
        notuse.add(6);
        notuse.add(7);
        notuse.add(8);
        notuse.add(9);

        notuse.add(10);
        notuse.add(11);

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.tab_fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "您点击了fab",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ReverseTab.class);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("current_tabs",current);
                bundle.putIntegerArrayList("notuse_tabs",notuse);
                intent.putExtras(bundle);

                startActivityForResult(intent,0);

            }
        });
    }

    public void initViewPage() {

        sectionsPagerAdapter = new SectionsPagerAdapter(this, current,notuse,getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void initImageLoader(){

    }
    public void initLogin(){
       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       navigationView.bringToFront();
       View headview=navigationView.inflateHeaderView(R.layout.nav_header_main);

        ImageView head_iv= (ImageView) headview.findViewById(R.id.imageView);
        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "您点击了头像",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        }


    public void initDrawer(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


}
