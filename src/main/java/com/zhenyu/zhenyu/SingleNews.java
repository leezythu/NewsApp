package com.zhenyu.zhenyu;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhenyu.zhenyu.Database.AppDatabase;
import com.zhenyu.zhenyu.Database.NewsEntity;
import com.zhenyu.zhenyu.user.UserProfile;
import com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil.saveImageToGallery;
import static com.zhenyu.zhenyu.utils.ShareMultiImageToWeChatUtil.shareWechatFriend;
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
//    private TextView titleview;
//    private TextView contentview;
    private ImageView imageView;
    private NewsEntity entity;
    private VideoView videoView;

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

        if(!imgins) {
            initImageLoader();
            imgins = true;
        }
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
        imageView = findViewById(R.id.image2);
        List<String> imgurl = entity.getImage();
        if(imgurl.size() < 1) {
            imageView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "image doesn't exist", Toast.LENGTH_LONG).show();
        }else{
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imgurl.get(0), imageView);
            Toast.makeText(getApplicationContext(), imgurl.get(0), Toast.LENGTH_LONG).show();
        }

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
        entity.setEntryTime(new Date().getTime());
        dataRepository.addNewsToBrowsedNews(entity);

//        Toast.makeText(getApplicationContext(), userProfile.seeCategory(), Toast.LENGTH_SHORT).show();

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
                    if (entity.getImage().size() < 1) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, entity.getTitle());
                        startActivity(Intent.createChooser(intent, "share"));
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
                                if(loadedImage==null){
                                    Toast.makeText(getApplicationContext(), "null bitmap", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                checkPermission();
//                                Uri uri = saveImageToGallery(SingleNews.this, loadedImage);
                                Uri uri = getImageUri(SingleNews.this, loadedImage);
                                shareWechatFriend(SingleNews.this, "what are you doing", uri);
//                                shareWechatMoment(SingleNews.this, "sing a song", uri);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
                        shareWechatFriend(SingleNews.this, entity.getTitle(), Uri.parse(entity.getImage().get(0)));
//                        shareWechatFriend(SingleNews.this, entity.getTitle(),resourceIdToUri(getApplicationContext(), R.drawable.round_dashboard_24));
                    }
                }
//                Toast.makeText(getApplicationContext(), "click share", Toast.LENGTH_LONG).show();
                break;
            case R.id.favorate_item:
                entity.setFlag(1);
                entity.setEntryTime(new Date().getTime());
                dataRepository.addNewsToBrowsedNews(entity);
                UserProfile userProfile = UserProfile.getInstance();
                userProfile.addFavorate(entity.getCategories(), entity.getKeyscore());
                Toast.makeText(getApplicationContext(), "added to favorates", Toast.LENGTH_LONG).show();
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

    private Uri getImageUri(Context context, Bitmap inImage) {

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");

        Uri path=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        if(path != null){
            OutputStream imageout = null;
            try{
                imageout = getContentResolver().openOutputStream(path);
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, imageout);
                imageout.close();
            }catch (IOException e){

            }
        }
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return path;
    }

    private void checkPermission(){
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showDialog("External storage", this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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

    public void initImageLoader() {
        // 获取默认的路径

        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                // 设置内存图片的宽高
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                // 缓存到磁盘中的图片宽高
                .diskCacheExtraOptions(480, 800, null)
                // .taskExecutor(null)
                // .taskExecutorForCachedImages()
                .threadPoolSize(3)
                // default 线程优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // // default设置在内存中缓存图像的多种尺寸
                //加载同一URL图片时,imageView从小变大时,从内存缓存中加载
                .denyCacheImageMultipleSizesInMemory()
                // 超过设定的缓存大小时,内存缓存的清除机制
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                // 内存的一个大小
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                // default 将图片信息缓存到该路径下
                .diskCache(new UnlimitedDiskCache(cacheDir))
                // default 磁盘缓存的大小
                .diskCacheSize(50 * 1024 * 1024)
                // 磁盘缓存文件的个数
                .diskCacheFileCount(100)
                //磁盘缓存的文件名的命名方式//一般使用默认值 (获取文件名称的hashcode然后转换成字符串)或MD5 new Md5FileNameGenerator()源文件的名称同过md5加密后保存
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // 设置默认的图片加载
                .imageDownloader(
                        new BaseImageDownloader(getApplicationContext())) // default
                // 使用默认的图片解析器
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

}
