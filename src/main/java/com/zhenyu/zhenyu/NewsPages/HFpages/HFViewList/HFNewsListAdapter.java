package com.zhenyu.zhenyu.NewsPages.HFpages.HFViewList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhenyu.zhenyu.Database.BrowsedNews;
import com.zhenyu.zhenyu.R;

import java.util.List;

public class HFNewsListAdapter extends RecyclerView.Adapter<HFListHolder> {
    List<BrowsedNews> currentNews;

    @NonNull
    @Override
    public HFListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item1, null);
        return new HFListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HFListHolder holder, int position) {
        holder.bindData(currentNews.get(position));
    }

    @Override
    public int getItemCount() {
        return currentNews == null?0:currentNews.size();
    }

    public void setNewsData(List<BrowsedNews> newdata) {
        if(currentNews == null) {
            currentNews = newdata;
            notifyItemRangeInserted(0, currentNews.size());
        }else{
            currentNews = newdata;
        }
        notifyDataSetChanged();
    }

    public String getHolderId(int position){
        return currentNews.get(position).getNewsid();
    }
    public BrowsedNews getAnews(int position){ return  currentNews.get(position);}
}
