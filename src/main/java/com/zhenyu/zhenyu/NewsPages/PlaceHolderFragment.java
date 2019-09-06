package com.zhenyu.zhenyu.NewsPages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhenyu.zhenyu.DataRepository;
import com.zhenyu.zhenyu.MainActivity;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.NewsListAdapter;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.RecyclerItemClickListener;
import com.zhenyu.zhenyu.SingleNews;
import com.zhenyu.zhenyu.utils.DateControl;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    private HashSet<Integer> yourChoices = new HashSet<>();



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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm-ss");
                String nowtime = simpleDateFormat.format(new Date());
                String enddate = dateControl.getFormatDate();
                String startdate = dateControl.backday();
                if(CategoryS.equals("首页"))
                    Reception.request(null, null, null, nowtime, 0);
                else if(CategoryS.equals("推荐")) {
                        Reception.requestRecommended(null, null, null, nowtime);
                        Reception.requestRecommended(null, null, null, nowtime);
                        Reception.requestRecommended(null, null, null, nowtime);
                }
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
                else if(CategoryS.equals("推荐")) {
                    Reception.requestRecommended(null, null, startdate, enddate);
                }
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
                        String newpageid = mnewsAdapter.getHolderId(position);
                        NewsEntity newsEntity = dataRepository.loadNewsById(newpageid);
                        List<String> params = new ArrayList<>(newsEntity.getKeyscore().keySet());
                        String[] paramstr  = new String[4];

                        try {
                            List<NewsEntity> ss = dataRepository.getSearchResult(new String(params.get(0).getBytes(), StandardCharsets.UTF_8)).getValue();
                            if (ss == null) {
                                Toast.makeText(getContext(), "length:null", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getContext(), "length:" + ss.size(), Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        for(int i = 0 ; i < 4; i++)
                            paramstr[i] = "不想看: "+params.get(i);
                        showMultiChoiceDialog(paramstr);

//                        dataRepository.removeById(newpageid);
                        try {
                            for (Integer w : yourChoices) {
                                dataRepository.removeByKeywords(new String(params.get(w).getBytes("GB2312"), StandardCharsets.UTF_8));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        mnewsAdapter.removed();

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

    private void showMultiChoiceDialog(String[] keys) {

        final String[] items = keys;
        // 设置默认选中的选项，全为false默认均未选中
        final boolean initChoiceSets[]={false,false,false,false};
        yourChoices.clear();
        AlertDialog.Builder multiChoiceDialog =
                new AlertDialog.Builder(getContext());
//        multiChoiceDialog.setTitle("屏蔽");
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            yourChoices.add(which);
                        } else {
                            yourChoices.remove(which);
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int size = yourChoices.size();

                        StringBuilder str = new StringBuilder();
                        for(Integer w:yourChoices){
                            str.append(items[w]).append("; ;");
                        }
//                        Toast.makeText(getContext(),
//                                "你选中了" + str.toString(),
//                                Toast.LENGTH_SHORT).show();
                    }
                });
        multiChoiceDialog.show();
    }

}