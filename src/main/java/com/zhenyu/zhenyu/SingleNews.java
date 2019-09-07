package com.zhenyu.zhenyu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hankcs.hanlp.HanLP;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.user.UserProfile;
import com.zhenyu.zhenyu.utils.LogController;
import com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil;
import com.zhenyu.zhenyu.utils.shareUtils;
import com.zhenyu.zhenyu.utils.tools;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil.shareWechatImages;
import static com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil.shareWechatMoment;

public class SingleNews extends AppCompatActivity implements View.OnClickListener{
    private static boolean imgins = false;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private String newsid;
    private DataRepository dataRepository;
    private LinearLayout bottomsheetlayout;
    private BottomSheetDialog bottomSheetBehavior;
    private LinearLayout sharelayout;
    private LinearLayout favoratelayout;
    private LinearLayout blockinglayout;
    private LinearLayout sharemomentslayout;
    private NewsEntity entity;
    private VideoView videoView;
    private LogController logController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserProfile profile = UserProfile.getInstance();
        if(profile.getThememode().equals("1"))
            setTheme(R.style.ThemeNight);
        else {
            setTheme(R.style.Default);
        }

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
        logController = LogController.getInstance(null);
        entity = dataRepository.loadNewsById(newsid);

        try {
            entity.getImage().size();
        }catch (Exception e){
            Gson gson = new Gson();
            Type mapType = new TypeToken<NewsEntity>() {}.getType();
            entity = gson.fromJson(bundle.getString("object"), mapType);
        }


        if(!imgins) {
            shareUtils.initImageLoader(getApplicationContext());
            imgins = true;
        }
        initpage();
        initbottonsheet();


        Button button = findViewById(R.id.pullbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click pull", Toast.LENGTH_LONG).show();
                Reception.syncToLocal("zhenyu");
            }
        });

        Button button1 = findViewById(R.id.clean);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "clean all", Toast.LENGTH_LONG).show();
                Reception.cleanall("zhenyu");
            }
        });
        Button button2 = findViewById(R.id.logout);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_LONG).show();
                Reception.usrLogout("zhenyu", "zhenyu");
            }
        });
    }
    public void initpage(){

        TextView titleview = findViewById(R.id.titleview);
        TextView contentview = findViewById(R.id.mycontentview);
        TextView timeView = findViewById(R.id.timeview);
        TextView sourceView = findViewById(R.id.sourceview);
        TextView contenview2 = findViewById(R.id.mycontentview2);
        TextView contentview3 = findViewById(R.id.mycontentview3);
        TextView contentview4 = findViewById(R.id.mycontentview4);
        //    private TextView titleview;
        //    private TextView contentview;


        String rawcontent = entity.getContent();
        Pattern pattern = Pattern.compile("\\n[\\s]*");
        Matcher matcher = pattern.matcher(rawcontent);
        String replacement = "\n        ";
        String parareplacement = "\n        #";
        StringBuffer strn = new StringBuffer();
        strn.append("        ");

        int huanhangcnt = 0;
        int paragraphcnt = 0;
        while (matcher.find()){
            huanhangcnt += 1;
            if(huanhangcnt % 2 == 0 && paragraphcnt < 3){
                paragraphcnt += 1;
                matcher.appendReplacement(strn, parareplacement);
            }else {
                matcher.appendReplacement(strn, replacement);
            }
        }
        matcher.appendTail(strn);
        String[] splitcontent = strn.toString().split("#");

        ImageView imageView = findViewById(R.id.image2);
        ImageView imageView1 = findViewById(R.id.image3);
        ImageView imageView2 = findViewById(R.id.image4);
        List<String> imgurl = entity.getImage();

        DisplayImageOptions options = tools.initOptions();
        if(imgurl.size() < 1) {
            imageView.setVisibility(View.GONE);
            StringBuilder buffer1 = new StringBuilder();
            for(int i = 0; i < splitcontent.length;i++)
                buffer1.append(splitcontent[i]);
            contentview.setText(buffer1.toString());
            contenview2.setVisibility(View.GONE);
            contentview3.setVisibility(View.GONE);
            contentview4.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "image doesn't exist", Toast.LENGTH_LONG).show();
        }else{
            ImageLoader imageLoader = ImageLoader.getInstance();
            switch (imgurl.size()) {
                case 0:
                    StringBuilder buffer1 = new StringBuilder();
                    for(int i = 0; i < splitcontent.length;i++)
                        buffer1.append(splitcontent[i]);
                    contentview.setText(buffer1.toString());
                    contenview2.setVisibility(View.GONE);
                    contentview3.setVisibility(View.GONE);
                    contentview4.setVisibility(View.GONE);
                    break;
                case 1:
                    contentview.setText(splitcontent[0]);
                    if(splitcontent.length == 2)
                        contenview2.setText(splitcontent[1]);
                    else if(splitcontent.length >= 3){
                        StringBuilder buffer = new StringBuilder();
                        for(int i = 1; i < splitcontent.length;i++)
                            buffer.append(splitcontent[i]);
                        contenview2.setText(buffer.toString());
                    }
                    else
                        contenview2.setVisibility(View.GONE);
                    contentview3.setVisibility(View.GONE);
                    contentview4.setVisibility(View.GONE);

                    imageLoader.displayImage(imgurl.get(0), imageView, options);
                    imageView1.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), imgurl.get(0), Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    contentview.setText(splitcontent[0]);
                    if(splitcontent.length == 1){
                        contenview2.setVisibility(View.GONE);
                        contentview3.setVisibility(View.GONE);
                    }
                    else if(splitcontent.length == 2){
                        contenview2.setText(splitcontent[1]);
                        contentview3.setVisibility(View.GONE);
                    }
                    else if(splitcontent.length == 3){
                        contenview2.setText(splitcontent[1]);
                        contentview3.setText(splitcontent[2]);
                    }
                    else{
                        contenview2.setText(splitcontent[1]);
                        StringBuilder buffer = new StringBuilder();
                        for(int i = 2; i < splitcontent.length;i++)
                            buffer.append(splitcontent[i]);
                        contentview3.setText(buffer.toString());
                    }
                    contentview4.setVisibility(View.GONE);

                    imageLoader.displayImage(imgurl.get(0), imageView, options);
                    imageLoader.displayImage(imgurl.get(1), imageView1, options);
                    imageView2.setVisibility(View.GONE);
                    break;
                default:
                    contentview.setText(splitcontent[0]);
                    if(splitcontent.length == 1){
                        contenview2.setVisibility(View.GONE);
                        contentview3.setVisibility(View.GONE);
                        contentview4.setVisibility(View.GONE);
                    }
                    if(splitcontent.length == 2) {
                        contenview2.setText(splitcontent[1]);
                        contentview3.setVisibility(View.GONE);
                        contentview4.setVisibility(View.GONE);
                    }
                    else if(splitcontent.length == 3){
                        contenview2.setText(splitcontent[1]);
                        contentview3.setText(splitcontent[2]);
                        contentview4.setVisibility(View.GONE);
                    }
                    else if(splitcontent.length == 4){
                        contenview2.setText(splitcontent[1]);
                        contentview3.setText(splitcontent[2]);
                        contentview4.setText(splitcontent[3]);
                    }else{
                        contenview2.setText(splitcontent[1]);
                        contentview3.setText(splitcontent[2]);
                        StringBuilder buffer = new StringBuilder();
                        for(int i = 3; i < splitcontent.length;i++)
                            buffer.append(splitcontent[i]);
                        contentview4.setText(buffer.toString());
                    }
                    imageLoader.displayImage(imgurl.get(0), imageView, options);
                    imageLoader.displayImage(imgurl.get(1), imageView1, options);
                    imageLoader.displayImage(imgurl.get(2), imageView2, options);
                    break;
            }
        }



        titleview.setText(entity.getTitle());
        timeView.setText(entity.getPublishTime());
        sourceView.setText(entity.getPublisher());


        videoView = (VideoView) findViewById(R.id.videos);
        //设置视频控制器,组件可以控制视频的播放，暂停，快进，组件，不需要你实现
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        String netPlayUrl="ht   tp://baobab.wdjcdn.com/145076769089714.mp4";
        Uri uri = Uri.parse(netPlayUrl);
        videoView.setVideoURI(uri);//设置视频的播放地址，网络地址。播放网络视频
//        //播放本地视频
//        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath()+"video.mp4");
        videoView.requestFocus();//让VideiView获取焦点
        videoView.start();//开始播放



        // handle user preference
        UserProfile userProfile = UserProfile.getInstance();
        userProfile.addkeys(entity.getCategories(), entity.getKeyscore());
        entity.setFlag(0);
        entity.setEntryTime(new Date().getTime());
        dataRepository.addNewsToBrowsedNews(entity);
//        entity.setHfflag(1);
//        dataRepository.addNewsViewed(entity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_single_news, menu);
        return true;
    }

    public void initbottonsheet(){
        bottomsheetlayout = (LinearLayout) findViewById(R.id.bottomsheet);
        bottomSheetBehavior = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        bottomSheetBehavior.setContentView(view);


        sharelayout = view.findViewById(R.id.share_item);
        favoratelayout = view.findViewById(R.id.favorate_item);
        blockinglayout = view.findViewById(R.id.blocking_item);
        sharemomentslayout = view.findViewById(R.id.share_moments);
        sharelayout.setOnClickListener(this);
        favoratelayout.setOnClickListener(this);
        blockinglayout.setOnClickListener(this);
        sharemomentslayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share_item:
//                if (!ShareMultiImageToWeChatUtil.isInstallWeChart(getApplicationContext())) {
//                    Toast.makeText(getApplicationContext(), "您没有安装微信", Toast.LENGTH_SHORT).show();
//                    return;
//                }else {}
                if (entity.getImage().size() < 1) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "#From NewestNews#\n"+entity.getTitle());
                    startActivity(Intent.createChooser(intent, "share"));
//                        shareText(getApplicationContext(), entity.getContent());
                } else {
                    ImageLoader.getInstance().loadImage(entity.getImage().get(0), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) { }
                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) { }
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            if(loadedImage==null){
                                Toast.makeText(getApplicationContext(), "null bitmap", Toast.LENGTH_LONG).show();
                                return;
                            }
                            checkPermission();
                            Uri uri = shareUtils.getImageUri(SingleNews.this, loadedImage);

                            List<String> targetText = HanLP.extractSummary(entity.getContent(), 2);

                            Toast.makeText(getApplicationContext(), targetText.get(0), Toast.LENGTH_LONG).show();


                            Bitmap textmap = shareUtils.textAsBitmap(entity.getContent(), 30);
                            Uri textUri = shareUtils.getImageUri(SingleNews.this, textmap);
                            ArrayList<Uri> urislist = new ArrayList<>();
                            urislist.add(uri);
                            urislist.add(textUri);
                            shareWechatImages(SingleNews.this, "sing a song", urislist);
//                                shareWechatMoment(SingleNews.this, "sing a song", uri);
//                                shareWechatFriend(SingleNews.this, "what are you doing", uri);
                        }
                        @Override
                        public void onLoadingCancelled(String imageUri, View view) { }
                    });
                }

//                Toast.makeText(getApplicationContext(), "click share", Toast.LENGTH_LONG).show();
                break;
            case R.id.share_moments:
                Toast.makeText(getApplicationContext(), "click moments", Toast.LENGTH_LONG).show();
                if (!ShareMultiImageToWeChatUtil.isInstallWeChart(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "您没有安装微信", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (entity.getImage().size() < 1) {
                        Bitmap textmap = shareUtils.textAsBitmap("#From NewestNews#\n"+entity.getContent(), 30);
                        Uri textUri = shareUtils.getImageUri(SingleNews.this, textmap);
                        shareWechatMoment(SingleNews.this, "friends", textUri);
//                        shareText(getApplicationContext(), entity.getContent());
                    } else {
                        ImageLoader.getInstance().loadImage(entity.getImage().get(0), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if (loadedImage == null) {
                                    Toast.makeText(getApplicationContext(), "null bitmap", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                checkPermission();
                                Uri uri = shareUtils.getImageUri(SingleNews.this, loadedImage);
//                                Bitmap textmap = shareUtils.textAsBitmap(entity.getContent(), 30);
//                                Uri textUri = shareUtils.getImageUri(SingleNews.this, textmap);
//                                ArrayList<Uri> urislist = new ArrayList<>();
//                                urislist.add(uri);
//                                urislist.add(textUri);
                                shareWechatMoment(SingleNews.this, "sing a song", uri);
//                                shareWechatMoment(SingleNews.this, "sing a song", uri);
//                                shareWechatFriend(SingleNews.this, "what are you doing", uri);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                            }
                        });
                    }
                }
                break;
            case R.id.favorate_item:
                entity.setFlag(1);
                entity.setEntryTime(new Date().getTime());
                dataRepository.addNewsToBrowsedNews(entity);
                UserProfile userProfile = UserProfile.getInstance();
                userProfile.addFavorate(entity.getCategories(), entity.getKeyscore());
                if(logController.getOnline().getValue()) {
                    Gson gson = new Gson();
                    Reception.uploadItem(LogController.getInstance(null).getUsername(), entity.getNewsid(), gson.toJson(entity));
                    Toast.makeText(getApplicationContext(), "added to favorates", Toast.LENGTH_LONG).show();
                }
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



    private void checkPermission(){
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showDialog("External storage", this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // Permission has already been granted
            Toast.makeText(this, "Already has the permission", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
