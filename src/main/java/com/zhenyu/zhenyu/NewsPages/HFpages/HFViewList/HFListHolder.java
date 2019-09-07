package com.zhenyu.zhenyu.NewsPages.HFpages.HFViewList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhenyu.zhenyu.Database.BrowsedNews;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.NewsPages.ViewListNews.RecyclerItemClickListener;
import com.zhenyu.zhenyu.R;
import com.zhenyu.zhenyu.utils.tools;

import java.util.List;
import java.util.Random;

public class HFListHolder extends RecyclerView.ViewHolder {
    private TextView titleView;
    private TextView dateView;
    private TextView categoryv;
    private ImageView imageView;
    private String idx;

    public HFListHolder(@NonNull View itemview){
        super(itemview);
        titleView = (TextView)itemview.findViewById(R.id.news_title);
        dateView = (TextView)itemview.findViewById(R.id.news_date);
        categoryv = (TextView)itemview.findViewById(R.id.news_category);
        imageView=(ImageView)itemview.findViewById(R.id.new_image);
    }

    public void bindData(BrowsedNews newsEntity){
        titleView.setText(newsEntity.getTitle());
        dateView.setText(newsEntity.getPublishTime());
        categoryv.setText(newsEntity.getCategories());
        idx = newsEntity.getNewsid();

        Random random = new Random();


        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
//        imageLoader.displayImage(imageUri, imageView);

        DisplayImageOptions options = tools.initOptions();
        List<String> imgurl = newsEntity.getImage();
        if(imgurl.size() < 1) {
            int choice = 109;
            int bigimg = random.nextInt(2);
            switch (bigimg){
                case 0:
                    choice = 1057 + random.nextInt(26);
                    break;
                case 1:
                    choice = 151 + random.nextInt(50);
                    break;
            }
            String imageUri = "https://picsum.photos/id/"+ choice +"/200";
            imageLoader.displayImage(imageUri, imageView, options);
//            imageView.setVisibility(View.GONE);
        }else{
            imageLoader.displayImage(imgurl.get(0), imageView, options);
        }

    }
    public String getIdx(){
        return idx;
    }
}
