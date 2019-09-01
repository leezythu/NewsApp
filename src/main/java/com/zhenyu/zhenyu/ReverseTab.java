package com.zhenyu.zhenyu;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenyu.zhenyu.Tab.ChannelItem;
import com.zhenyu.zhenyu.Tab.DragAdapter;
import com.zhenyu.zhenyu.Tab.DragGrid;
import com.zhenyu.zhenyu.Tab.OtherAdapter;
import com.zhenyu.zhenyu.Tab.OtherGridView;

/**
 * 频道管理

 */
public class ReverseTab extends Activity implements OnItemClickListener{
    /** 用户栏目的GRIDVIEW */
    private DragGrid userGridView;
    /** 其它栏目的GRIDVIEW */
    private OtherGridView otherGridView;
    /** 用户栏目对应的适配器，可以拖动 */
    DragAdapter userAdapter;
    /** 其它栏目对应的适配器 */
    OtherAdapter otherAdapter;
    /** 其它栏目列表 */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    /** 用户栏目列表 */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    boolean isMove = false;
    private TextView tv_save;
    private  ArrayList<Integer>current;
    private ArrayList<Integer>notuse;
    private Map<Integer,String> mymap=new HashMap<Integer,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        current=bundle.getIntegerArrayList("current_tabs");
        notuse=bundle.getIntegerArrayList("notuse_tabs");
        initView();
        initData();
    }

    /** 初始化数据*/
    private void initData() {
        mymap.put(0,"首页");
        mymap.put(1,"推荐");

        mymap.put(2,"科技");
        mymap.put(3,"娱乐");
        mymap.put(4,"军事");
        mymap.put(5,"体育");
        mymap.put(6,"财经");
        mymap.put(7,"健康");
        mymap.put(8,"教育");
        mymap.put(9,"社会");
        mymap.put(10,"汽车");
        mymap.put(11,"文化");



        for(Integer i:current){
            ChannelItem tmp=new ChannelItem(i,mymap.get(i),0 ,1 ,0);
            userChannelList.add(tmp);
        }
        for(Integer i:notuse){
            ChannelItem tmp=new ChannelItem(i,mymap.get(i),0 ,0 ,0);
            otherChannelList.add(tmp);
        }
//        userChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
//        otherChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getOtherChannel());
        userAdapter = new DragAdapter(this, userChannelList,userGridView);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
        userAdapter.setOnDelecteItemListener(new DragAdapter.OnDelecteItemListener() {
            @Override
            public void onDelete(final int position, View v, ViewGroup parent) {
                if (position != 0) {
                    View view = userGridView.getChildAt(position);
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = userAdapter.getItem(position);//获取点击的频道内容
                        otherAdapter.setVisible(false);
                        userAdapter.setIsDeleteing(true);
                        //添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
            }
        });

        userAdapter.setOnStartDragingListener(new DragAdapter.OnStartDragingListener() {
            @Override
            public void onStartDraging() {
                tv_save.setVisibility(View.VISIBLE);
            }
        });
    }

    /** 初始化布局*/
    private void initView() {

        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAdapter.hideDeleteIcon(true);
                userAdapter.showDeleteIcon(false);
                userAdapter.notifyDataSetChanged();
                tv_save.setVisibility(View.GONE);
            }
        });
        TextView v=(TextView)findViewById(R.id.my_category_text);
        v.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "您点击了!!",Toast.LENGTH_LONG).show();
//                System.out.println(userAdapter.channelList);
                Toast.makeText(getApplicationContext(), "您点击了!!"+userAdapter.channelList,Toast.LENGTH_LONG).show();
            }
        });

       TextView iv=(TextView)findViewById(R.id.back_btn);
        iv.bringToFront();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "您点击了!!",Toast.LENGTH_LONG).show();
//                System.out.println(userAdapter.channelList);
//                Toast.makeText(getApplicationContext(), "返回??",Toast.LENGTH_LONG).show();
                Intent intent =new Intent();
                Bundle bundle=new Bundle();
                ArrayList<Integer>current_tmp=new  ArrayList<Integer>() ;
                ArrayList<Integer>notuse_tmp= new ArrayList<Integer>();
                for(ChannelItem item:userAdapter.channelList){
                    current_tmp.add(item.id);
                }
                for(ChannelItem item:otherAdapter.channelList){
                    notuse_tmp.add(item.id);
                }current=current_tmp;
                notuse=notuse_tmp;
                bundle.putIntegerArrayList("current_tabs",current);
                bundle.putIntegerArrayList("notuse_tabs",notuse);
                intent.putExtras(bundle);
                setResult(0,intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /** GRIDVIEW对应的ITEM点击监听接口  */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if(isMove){
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                //TODO position为 0的不可以进行任何操作
                Toast.makeText(getBaseContext(),"item点击",Toast.LENGTH_SHORT).show();
//			if (position != 0) {
//				final ImageView moveImageView = getView(view);
//				if (moveImageView != null) {
//					TextView newTextView = (TextView) view.findViewById(R.id.text_item);
//					final int[] startLocation = new int[2];
//					newTextView.getLocationInWindow(startLocation);
//					final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
//					otherAdapter.setVisible(false);
//					//添加到最后一个
//					otherAdapter.addItem(channel);
//					new Handler().postDelayed(new Runnable() {
//						public void run() {
//							try {
//								int[] endLocation = new int[2];
//								//获取终点的坐标
//								otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
//								MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
//								userAdapter.setRemove(position);
//							} catch (Exception localException) {
//							}
//						}
//					}, 50L);
//				}
//			}
                break;
            case R.id.otherGridView:
                final ImageView moveImageView = getView(view);
                if (moveImageView != null){
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    channel.setNewItem(1);
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation , endLocation, channel,otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 点击ITEM移动动画
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                }else{
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

//    /** 退出时候保存选择后数据库的设置  */
//    private void saveChannel() {
//        ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).deleteAllChannel();
//        ArrayList<ChannelItem> items = new ArrayList<ChannelItem>();
//        for (ChannelItem item :userAdapter.getChannnelLst()){
//            item.setNewItem(0);
//            items.add(item);
//        }
//        ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveUserChannel(items);
//        ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveOtherChannel(otherAdapter.getChannnelLst());
//    }

//    @Override
//    public void onBackPressed() {
////        saveChannel();
//
//        super.onBackPressed();
//    }
}
