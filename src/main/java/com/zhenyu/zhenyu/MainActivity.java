package com.zhenyu.zhenyu;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.AppExecutors;
import com.zhenyu.zhenyu.NewsPages.SectionsPagerAdapter;
import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.user.UserProfile;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private SectionsPagerAdapter sectionsPagerAdapter;


    private ArrayList<Integer> current = new ArrayList<Integer>();
    private ArrayList<Integer> notuse = new ArrayList<Integer>();
    String nightmode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTheme(R.style.Default);
        nightmode = "0";
        setContentView(R.layout.activity_main);
        AppDatabase.getDatabase(getApplicationContext(), new AppExecutors());
        DataRepository.getInstance(AppDatabase.getDatabase(getApplicationContext(), null));
        Reception.getReception(getApplication());

        initDrawer();
        initTabs();

        initViewPage();
        initLogin();

        init_night();
        init_day();
        init_clear();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();
        current = bundle.getIntegerArrayList("current_tabs");
        notuse = bundle.getIntegerArrayList("notuse_tabs");
        Toast.makeText(getApplicationContext(), current.toString(), Toast.LENGTH_LONG).show();

        sectionsPagerAdapter.set_cur(current);
        sectionsPagerAdapter.set_notuse(notuse);
        sectionsPagerAdapter.notifyDataSetChanged();
        initViewPage();
    }



    public void initTabs() {

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tab_fab);

        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "您点击了fab",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ReverseTab.class);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("current_tabs", current);
                bundle.putIntegerArrayList("notuse_tabs", notuse);
                intent.putExtras(bundle);

                startActivityForResult(intent, 0);

            }
        });
    }

    public void initViewPage() {

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, current, notuse, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void initImageLoader() {

    }

    public void initLogin() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();

        View headview = navigationView.inflateHeaderView(R.layout.nav_header_main);

        ImageView head_iv = (ImageView) headview.findViewById(R.id.imageView);
        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "您点击了头像",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }


    public void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_night,
                R.id.nav_tools, R.id.nav_collection, R.id.nav_send,R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Menu menu = navigationView.getMenu();
        MenuItem coll = menu.findItem(R.id.nav_collection);
        coll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "您点击了头像", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem night = menu.findItem(R.id.nav_night);
        night.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "您点击了夜间模式", Toast.LENGTH_LONG).show();
                setTheme(R.style.ThemeNight);
                NavigationView navigationView = findViewById(R.id.nav_view);
                TabLayout tabs = findViewById(R.id.tabs);
                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setBackgroundColor(Color.BLACK);
                tabs.setBackgroundColor(Color.BLACK);
                navigationView.setBackgroundColor(Color.BLACK);

                initDrawer();
                initTabs();
                initViewPage();
//                initLogin();
                return true;
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getApplicationContext(), "您点击了头像", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SearchHistory.class);
                Bundle bundle = new Bundle();
                String keyword="null";
                bundle.putString("keyword",keyword);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    public void init_night(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem night = menu.findItem(R.id.nav_night);
        Drawable newIcon = (Drawable)night.getIcon();
        newIcon.mutate().setColorFilter(Color.argb(255, 200, 200, 200), PorterDuff.Mode.SRC_IN);
        night.setIcon(newIcon);
        night.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                UserProfile userProfile = UserProfile.getInstance();
                userProfile.setThememode("1");
                nightmode="1";
                Toast.makeText(getApplicationContext(), "您点击了夜间模式", Toast.LENGTH_LONG).show();
                setTheme(R.style.ThemeNight);
                NavigationView navigationView = findViewById(R.id.nav_view);
                TabLayout tabs = findViewById(R.id.tabs);
                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setBackgroundColor(Color.BLACK);
                tabs.setBackgroundColor(Color.BLACK);
                navigationView.setBackgroundColor(Color.BLACK);

                initDrawer();
//                initTabs();
                initViewPage();
//                initLogin();
                return true;
            }
        });
//        Toast.makeText(getApplicationContext(), "nightnightmode"+nightmode, Toast.LENGTH_LONG).show();
    }

    public void init_day(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem day = menu.findItem(R.id.nav_day);
        Drawable newIcon = (Drawable)day.getIcon();
        newIcon.mutate().setColorFilter(Color.argb(255, 200, 200, 200), PorterDuff.Mode.SRC_IN);
        day.setIcon(newIcon);
        day.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                nightmode="0";
                UserProfile userProfile = UserProfile.getInstance();
                userProfile.setThememode("0");
                Toast.makeText(getApplicationContext(), "您点击了白天模式", Toast.LENGTH_LONG).show();
                setTheme(R.style.Default);
                NavigationView navigationView = findViewById(R.id.nav_view);
                TabLayout tabs = findViewById(R.id.tabs);
                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setBackgroundColor(Color.parseColor("#8A2BE2"));
                tabs.setBackgroundColor(Color.parseColor("#9370DB"));
                navigationView.setBackgroundColor(Color.WHITE);
                initDrawer();
//                initTabs();
                initViewPage();
//                initLogin();
                return true;
            }
        });
    }

    public void init_clear(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem clear = menu.findItem(R.id.nav_clear);
        Drawable newIcon = (Drawable)clear.getIcon();
        newIcon.mutate().setColorFilter(Color.argb(255, 200, 200, 200), PorterDuff.Mode.SRC_IN);
        clear.setIcon(newIcon);
        clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "您点击了清除缓存", Toast.LENGTH_LONG).show();
                //add your code here

                initDrawer();
                initTabs();
                initViewPage();
//                initLogin();
                return true;
            }
        });
    }

}
