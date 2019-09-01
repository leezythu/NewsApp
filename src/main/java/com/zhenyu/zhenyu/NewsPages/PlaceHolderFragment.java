package com.zhenyu.zhenyu.NewsPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.NewsListAdapter;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.RecyclerItemClickListener;
import com.zhenyu.zhenyu.SingleNews;
import com.zhenyu.zhenyu.utils.DateControl;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceHolderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_CATEGORY = "section_category";

    private PageViewModel pageViewModel;

    private int cnt = 0;
    private DataRepository dataRepository;
    private NewsListAdapter mnewsAdapter;
    private String CategoryS;
    private DateControl dateControl;
    private int mflag;


    public static PlaceHolderFragment newInstance(int index, String category) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(ARG_SECTION_CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        if (getArguments() != null) {
            CategoryS = getArguments().getString(ARG_SECTION_CATEGORY);
        }
        pageViewModel.setCategoricalLiveData(CategoryS);
        Reception.getReception(getContext());
        dataRepository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));
        mnewsAdapter = new NewsListAdapter();
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

                String enddate = dateControl.getFormatDate();
                String startdate = dateControl.backday();
                if(CategoryS.equals("首页"))
                    Reception.request(null, null, startdate, enddate, 0);
                else if(CategoryS.equals("推荐"))
                    Reception.requestRecommended(null, null, startdate, enddate);
                else
                    Reception.request(null, CategoryS, startdate, enddate, pageViewModel.getMflag());
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                String enddate = dateControl.getFormatDate();
                String startdate = dateControl.backday();
                if(CategoryS.equals("首页"))
                    Reception.request(null, null, startdate, enddate, 0);
                else if(CategoryS.equals("推荐"))
                    Reception.requestRecommended(null, null, startdate, enddate);
                else
                    Reception.request(null, CategoryS, startdate, enddate, pageViewModel.getMflag());
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

                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        pageViewModel.getNews().observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> newsEntities) {
                String w = "";
                try{
                    w = newsEntities.get(0).getTitle();
                    mnewsAdapter.setNewsData(pageViewModel.getNews().getValue());
//                    textView.setText(cnt + " " + w + dataRepository.getDatabaseSize() + " " + pageViewModel.getsize());
                    cnt += 1;
                }catch (Exception e){
//                    textView.setText("error in viewModel," + e.getMessage());
                }

            }
        });


        return root;
    }
}