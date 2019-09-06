package com.zhenyu.zhenyu.NewsPages.ViewListNews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.utils.tools;

import org.w3c.dom.Text;

import java.util.List;

public class NewsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView titleView;
    private TextView dateView;
    private TextView categoryv;
    private ImageView imageView;
    private CardView cardView;
    private String idx;

    public NewsListHolder(@NonNull View itemview){
        super(itemview);
        titleView = (TextView)itemview.findViewById(R.id.news_title);
        dateView = (TextView)itemview.findViewById(R.id.news_date);
        categoryv = (TextView)itemview.findViewById(R.id.news_category);
        imageView=(ImageView)itemview.findViewById(R.id.new_image);
        cardView=(CardView)itemview.findViewById(R.id.news_card);
    }

    public void bindData(NewsEntity newsEntity){
        titleView.setText(newsEntity.getTitle());
        dateView.setText(newsEntity.getPublishTime());
        categoryv.setText(newsEntity.getCategories());
        idx = newsEntity.getNewsid();
        String imageUri = "https://hellorfimg.zcool.cn/preview260/224921422.jpg";
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

        //浏览过的新闻变为灰色
        if(newsEntity.getHfflag() == 1)
            titleView.setTextColor(Color.GRAY);

        DisplayImageOptions options = tools.initOptions();
        List<String> imgurl = newsEntity.getImage();
        if(imgurl.size() < 1) {
            imageLoader.displayImage(imageUri, imageView, options);
//            imageView.setVisibility(View.GONE);
        }else{
            imageLoader.displayImage(imgurl.get(0), imageView, options);
        }
    }
    public String getIdx(){
        return idx;
    }

    @Override
    public void onClick(View view){

    }
}
