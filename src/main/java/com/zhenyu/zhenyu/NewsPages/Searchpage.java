package com.zhenyu.zhenyu.NewsPages;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.NewsListAdapter;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.RecyclerItemClickListener;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.SingleNews;

import java.util.List;

public class Searchpage extends Fragment {
    private List<NewsEntity> result;
    private NewsListAdapter mnewsAdapter;

    public static PlaceHolderFragment newInstance(int index, String keys) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("KeyWords", keys);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        DataRepository dataRepository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));
        String keys = savedInstance.getString("KeyWords");
        result = dataRepository.getSearchResult(keys).getValue();
        if(result == null){
            Toast.makeText(getContext(), "null object", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getContext(), "get historical news", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mnewsAdapter = new NewsListAdapter();

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
        return root;
    }
}
