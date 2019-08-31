package com.zhenyu.zhenyu.NewsPages.ViewListNews;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.Database.NewsEntity;

import org.w3c.dom.Text;

public class NewsListHolder extends RecyclerView.ViewHolder {
    private TextView titleView;
    private TextView dateView;
    private TextView categoryv;
    private String idx;

    public NewsListHolder(@NonNull View itemview){
        super(itemview);
        titleView = (TextView)itemview.findViewById(R.id.news_title);
        dateView = (TextView)itemview.findViewById(R.id.news_date);
//        categoryv = (TextView)itemview.findViewById(R.id.categoryview);
    }

    public void bindData(NewsEntity newsEntity){
        titleView.setText(newsEntity.getTitle());
        dateView.setText(newsEntity.getPublishTime());
//        categoryv.setText("categoty:"+newsEntity.getCategories());
        idx = newsEntity.getNewsid();

    }
    public String getIdx(){
        return idx;
    }
}
