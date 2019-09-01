package com.zhenyu.zhenyu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil;
import com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.user.UserProfile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil.resourceIdToUri;
import static com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil.shareText;

public class SingleNews extends AppCompatActivity implements View.OnClickListener{

    private String newsid;
    private DataRepository dataRepository;
    private LinearLayout bottomsheetlayout;
    private BottomSheetDialog bottomSheetBehavior;
    private LinearLayout sharelayout;
    private LinearLayout favoratelayout;
    private LinearLayout blockinglayout;
//    private TextView titleview;
//    private TextView contentview;
    private ImageView moreview;
    private NewsEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("NewsApp");
        toolbar.bringToFront();
//        toolbar.setSubtitle("这里是子标题");
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
//        toolbar.setLogo(R.drawable.round_more_horiz_24  );
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_single_news);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_settings:
                        bottomSheetBehavior.show();
                        break;
                    case R.id.action_search:
                        Toast.makeText(getApplicationContext(), "searching~", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }

        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        newsid = bundle.getString("newsid");
        dataRepository = DataRepository.getInstance(AppDatabase.getDatabase(null, null));

        initpage();
        initbottonsheet();
    }
    public void initpage(){
        entity = dataRepository.loadNewsById(newsid);
//        Toast.makeText(getApplicationContext(), entity.getContent(), Toast.LENGTH_LONG).show();
//        assert titleview != null;

        TextView titleview = findViewById(R.id.titleview);
        TextView contentview = findViewById(R.id.mycontentview);
        TextView timeView = findViewById(R.id.timeview);
        TextView sourceView = findViewById(R.id.sourceview);

        String rawcontent = entity.getContent();
        Pattern pattern = Pattern.compile("[\\n\\s]+");
        Matcher matcher = pattern.matcher(rawcontent);
        String replacement = "\n        ";
        StringBuffer strn = new StringBuffer();
        strn.append("        ");
        while (matcher.find()){
            matcher.appendReplacement(strn, replacement);
        }
        matcher.appendTail(strn);

        titleview.setText(entity.getTitle());
        contentview.setText(strn.toString());
        timeView.setText(entity.getPublishTime());
        sourceView.setText(entity.getPublisher());

        // handle user preference
        UserProfile userProfile = UserProfile.getInstance();
        userProfile.addkeys(entity.getCategories(), entity.getKeyscore());
        entity.setFlag(0);
        dataRepository.addNewsToBrowsedNews(entity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_single_news, menu);
        return true;
    }

    public void initbottonsheet(){

//        moreview = findViewById(R.id.moreaction);
//        moreview.setOnClickListener(this);

        bottomsheetlayout = (LinearLayout) findViewById(R.id.bottomsheet);
        bottomSheetBehavior = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        bottomSheetBehavior.setContentView(view);

        sharelayout = view.findViewById(R.id.share_item);
        favoratelayout = view.findViewById(R.id.favorate_item);
        blockinglayout = view.findViewById(R.id.blocking_item);
        sharelayout.setOnClickListener(this);
        favoratelayout.setOnClickListener(this);
        blockinglayout.setOnClickListener(this);

//        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheetlayout);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share_item:
                if (!ShareMultiImageToWeChatUtil.isInstallWeChart(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "您没有安装微信", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (entity.getImage() == null || entity.getImage().equals("")) {
                        shareText(getApplicationContext(), entity.getContent());
                    } else {
                        ImageLoader imageLoader = ImageLoader.getInstance();
//                        Uri uri = ShareMultiImageToWeChatUtil.saveImageToGallery(getApplicationContext(),resource);
//                        ShareMultiImageToWeChatUtil.shareWithImage(getApplicationContext(), entity.getTitle(), uri);
                    }
                }
                Toast.makeText(getApplicationContext(), "click share", Toast.LENGTH_LONG).show();
                break;
            case R.id.favorate_item:
                entity.setFlag(1);
                dataRepository.addNewsToBrowsedNews(entity);
                Toast.makeText(getApplicationContext(), "added to favorate news", Toast.LENGTH_LONG).show();
                break;
            case R.id.blocking_item:
                Toast.makeText(getApplicationContext(), "click blocking", Toast.LENGTH_LONG).show();
                break;
//            case R.id.moreaction:
//                bottomSheetBehavior.show();
            default:
                    break;
        }

    }


}
