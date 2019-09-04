package com.zhenyu.zhenyu;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.zhenyu.zhenyu.NewsPages.PlaceHolderFragment;
import com.zhenyu.zhenyu.NewsPages.Searchpage;
import com.zhenyu.zhenyu.NewsPages.SectionsPagerAdapter;

public class Search extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        mSearchView = (SearchView) findViewById(R.id.searchView);
//        mListView = (ListView) findViewById(R.id.listView);
//        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
//        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),"搜索内容为：", Toast.LENGTH_LONG).show();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                PlaceHolderFragment fragment = Searchpage.newInstance(0, "天");
                /* SearchFragment fragment=new SearchFragment(); */
                transaction.add(R.id.search_res_fragment, (Fragment) fragment,"dynamicFragment");
                transaction.commit();
                return false;
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
}