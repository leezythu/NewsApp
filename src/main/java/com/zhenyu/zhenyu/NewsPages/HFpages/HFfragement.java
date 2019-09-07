package com.zhenyu.zhenyu.NewsPages.HFpages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.BrowsedNews;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.NewsPages.HFpages.HFViewList.HFNewsListAdapter;
import com.zhenyu.zhenyu.NewsPages.PlaceHolderFragment;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.RecyclerItemClickListener;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.SingleNews;
import com.zhenyu.zhenyu.utils.DateControl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class HFfragement  extends Fragment {
    private static final String ARG_SECTION_NUMBER = "HFsection_number";
    private static final String ARG_SECTION_CATEGORY = "HFsection_category";

    private HFviewModel hf_pageViewModel;

    private int cnt = 0;
    private DataRepository dataRepository;
    private HFNewsListAdapter mnewsAdapter;
    private String CategoryS;
    private DateControl dateControl;
    private int mflag;

    public static HFfragement newInstance(int index, String category) {
        HFfragement fragment = new HFfragement();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(ARG_SECTION_CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hf_pageViewModel = ViewModelProviders.of(this).get(HFviewModel.class);
        if (getArguments() != null) {
            CategoryS = getArguments().getString(ARG_SECTION_CATEGORY);
            hf_pageViewModel.setmObservableNews(CategoryS);
        }
        dataRepository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));
        mnewsAdapter = new HFNewsListAdapter();
        dateControl = new DateControl();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RefreshLayout refreshLayout = (RefreshLayout)root.findViewById(R.id.refreshlays);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });


        final RecyclerView newsRecycler = root.findViewById(R.id.newslist);
        newsRecycler.setAdapter(mnewsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecycler.setLayoutManager(layoutManager);

        //on response to touching event
        newsRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), newsRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(getContext(), "on click item:" + mnewsAdapter.getHolderId(position), Toast.LENGTH_LONG).show();

                        String newpageid = mnewsAdapter.getHolderId(position);
//                        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
//                            ((MainActivity) getActivity()).show(newpageid);
//                        }
                        Intent intent = new Intent(getContext(), SingleNews.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("newsid",newpageid);
                        Gson gson = new Gson();
                        String sobj = gson.toJson(mnewsAdapter.getAnews(position));
                        bundle.putString("object", sobj);

                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        hf_pageViewModel.getNews().observe(this, new Observer<List<BrowsedNews>>() {
            @Override
            public void onChanged(List<BrowsedNews> newsEntities) {
                String w = "";
                try{
                    w = newsEntities.get(0).getTitle();
                    mnewsAdapter.setNewsData(hf_pageViewModel.getNews().getValue());
//                    textView.setText(cnt + " " + w + dataRepository.getDatabaseSize() + " " + pageViewModel.getsize());
                    Toast.makeText(getContext(), "current news:"+String.valueOf(hf_pageViewModel.getsize()),Toast.LENGTH_LONG).show();
                }catch (Exception e){
//                    textView.setText("error in viewModel," + e.getMessage());
                }

            }
        });


        return root;
    }
}
