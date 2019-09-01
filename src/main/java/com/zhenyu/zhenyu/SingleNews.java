package com.zhenyu.zhenyu;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nostra13.universalimageloader.utils.L;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.user.UserProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        NewsEntity entity = dataRepository.loadNewsById(newsid);
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
                Toast.makeText(getApplicationContext(), "click share", Toast.LENGTH_LONG).show();
                break;
            case R.id.favorate_item:
                Toast.makeText(getApplicationContext(), "click favorate", Toast.LENGTH_LONG).show();
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
