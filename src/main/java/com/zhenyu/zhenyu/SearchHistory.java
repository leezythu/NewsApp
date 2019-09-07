package com.zhenyu.zhenyu;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.user.UserProfile;
import com.zhenyu.zhenyu.utils.ViewDialog;

import java.util.ArrayList;
import java.util.List;

public class SearchHistory extends AppCompatActivity {
    private SearchView mSearchView;
    private  ListView listView;
    private ViewDialog viewDialog;
    private UserProfile userProfile;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        userProfile = userProfile.getInstance();

        if(userProfile.getThememode().equals("1"))
            setTheme(R.style.ThemeNight);
        else {
            setTheme(R.style.Default);
        }
//        this.setTheme(R.style.Default);


        viewDialog = new ViewDialog(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_h_main);
        initsearch();

    }
    public void initsearch() {
        mSearchView = (SearchView) findViewById(R.id.searchView);
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "test query:"+query, Toast.LENGTH_LONG).show();
                key = query;
                Reception.searchForNews(key, getApplicationContext());
//                searchHistory.add(query);
                if(!userProfile.historyContains(query))
                    userProfile.addsearchHistory(query);
                viewDialog.showDialog();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //...here i'm waiting 5 seconds before hiding the custom dialog
                        //...you can do whenever you want or whenever your work is done
                        viewDialog.hideDialog();

                        Intent intent = new Intent(getApplicationContext(), Search.class);
                        Bundle bundle = new Bundle();
                        String keyword = key;
                        bundle.putString("keyword", keyword);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }, 1000);


                if (query.length() > 0) {
//                  Toast.makeText(getApplicationContext(), "key==" + key, Toast.LENGTH_LONG).show();
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


        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.listview_text,R.id.listview_text,userProfile.getSearchHistoty());
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