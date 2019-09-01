package com.zhenyu.zhenyu.NewsPages.ViewListNews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.Database.NewsEntity;

import org.w3c.dom.Text;

public class NewsListHolder extends RecyclerView.ViewHolder {
    private TextView titleView;
    private TextView dateView;
    private TextView categoryv;
    private ImageView imageView;
    private String idx;

    public NewsListHolder(@NonNull View itemview){
        super(itemview);
        titleView = (TextView)itemview.findViewById(R.id.news_title);
        dateView = (TextView)itemview.findViewById(R.id.news_date);
        categoryv = (TextView)itemview.findViewById(R.id.news_category);
        imageView=(ImageView)itemview.findViewById(R.id.new_image);
    }

    public void bindData(NewsEntity newsEntity){
        titleView.setText(newsEntity.getTitle());
        dateView.setText(newsEntity.getPublishTime());
        categoryv.setText(newsEntity.getCategories());
        idx = newsEntity.getNewsid();
        String imageUri = "https://hellorfimg.zcool.cn/preview260/224921422.jpg";
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.displayImage(imageUri, imageView);

    }
    public String getIdx(){
        return idx;
    }
}
