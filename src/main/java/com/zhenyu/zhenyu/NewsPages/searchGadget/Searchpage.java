package com.zhenyu.zhenyu.NewsPages.searchGadget;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.NewsListAdapter;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.RecyclerItemClickListener;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.SingleNews;

import java.util.List;

public class Searchpage extends Fragment {
    private List<NewsEntity> result;
    private NewsListAdapter mnewsAdapter;
    private searchViewModel viewModel;

    public static Searchpage newInstance(int index, String keys) {
        Searchpage fragment = new Searchpage();
        Bundle bundle = new Bundle();
        bundle.putString("KeyWords", keys);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Searchpage(){}

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        viewModel = ViewModelProviders.of(this).get(searchViewModel.class);
        String keys = "中国";
        if(getArguments() != null) {
            keys = getArguments().getString("KeyWords");
            keys = "中国";
            Toast.makeText(getContext(), "network request", Toast.LENGTH_LONG).show();
            Reception.request(keys, null, null, null, 3);
            viewModel.searchFor(keys);
        }
        Log.i("3","before new ");
        mnewsAdapter = new NewsListAdapter();
        Log.i("2","after new ");
//
//        if(result == null){
//            Toast.makeText(getContext(), "null object", Toast.LENGTH_LONG).show();
//        }
//        else{
//            Toast.makeText(getContext(), "get historical news", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Log.i("1","before recycl");
        final RecyclerView newsRecycler = root.findViewById(R.id.newslist);
        newsRecycler.setAdapter(mnewsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecycler.setLayoutManager(layoutManager);

        //on response to touching event
        newsRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), newsRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String newpageid = mnewsAdapter.getHolderId(position);
                        Intent intent = new Intent(getContext(), SingleNews.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("newsid",newpageid);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        viewModel.getSearchRes().observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> newsEntities) {
                mnewsAdapter.setNewsData(newsEntities);
                if(newsEntities == null){
                    Toast.makeText(getContext(), "no result", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "get result" + newsEntities.size(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }
}
