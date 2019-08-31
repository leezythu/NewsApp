package com.zhenyu.zhenyu.NewsPages.ViewListNews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.Database.NewsEntity;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListHolder> {
    List<NewsEntity> currentNews;

    @NonNull
    @Override
    public NewsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        return new NewsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListHolder holder, int position) {
        holder.bindData(currentNews.get(position));
    }

    @Override
    public int getItemCount() {
        return currentNews == null?0:currentNews.size();
    }

    public void setNewsData(List<NewsEntity> newdata) {
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
}
