package com.zhenyu.zhenyu;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
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
import com.zhenyu.zhenyu.NewsPages.ViewListNews.RecyclerItemClickListener;
import com.zhenyu.zhenyu.NewsPages.searchGadget.S_SectionsPagerAdapter;
import com.zhenyu.zhenyu.NewsPages.searchGadget.Searchpage;

import java.util.ArrayList;
import java.util.List;

public class SearchHistory extends AppCompatActivity {
    private SearchView mSearchView;
    private List<String> searchHistory=new ArrayList<String>();
    private  ListView listView;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTheme(R.style.Default);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_h_main);
        initsearch();

    }
    public void initsearch() {
        searchHistory.add("侯哥");
        searchHistory.add("你的小可爱");
        mSearchView = (SearchView) findViewById(R.id.searchView);
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHistory.add(query);
                key = query;
                Intent intent = new Intent(getApplicationContext(), Search.class);
                Bundle bundle = new Bundle();
                String keyword = query;
                bundle.putString("keyword", keyword);
                intent.putExtras(bundle);
                startActivity(intent);
                if (query.length() > 0) {
                    Toast.makeText(getApplicationContext(), "key==" + key, Toast.LENGTH_LONG).show();
                    mSearchView.setIconified(true);
                    return true;
                } else {
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


        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.listview_text,R.id.listview_text,searchHistory);
        listView = (ListView) findViewById(R.id.listView);
        listView.setDividerHeight(0);
        listView.setAdapter(adapter);

        //设置listview点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long positionid) {//arg1为当前的view，用当前的view
                // TODO Auto-generated method stub

                TextView t1= (TextView) arg1.findViewById(R.id.listview_text);
                String id=t1.getText().toString();
                Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
                mSearchView.setQuery(id,false);
            }
        });

    }
}