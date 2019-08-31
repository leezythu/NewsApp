package com.zhenyu.zhenyu.NewsPages;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zhenyu.zhenyu.R;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_shouye, R.string.tab_text_tuijian, R.string.tab_text_keji, R.string.tab_text_yule, R.string.tab_text_junshi,R.string.tab_text_tiyu,R.string.tab_text_caijing,R.string.tab_text_jiankang,R.string.tab_text_jiaoyu,R.string.tab_text_shehui, R.string.tab_text_qiche};
    private static final String[] TAB_CONTENT = new String[]{"首页","推荐","科技", "娱乐", "军事","体育","财经","健康","教育","社会", "汽车"};
    private final Context mContext;
    private ArrayList<Integer>cur;
    private ArrayList<Integer>notuse;
    private int count;
    public SectionsPagerAdapter(Context context, ArrayList<Integer>cur,ArrayList<Integer>notuse, FragmentManager fm) {
        super(fm);
        count=cur.size();
        this.cur=cur;
        this.notuse=notuse;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceHolderFragment.newInstance(position + 1,TAB_CONTENT[cur.get(position)]);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_CONTENT[cur.get(position)];
    }

    @Override

    public int getCount() {
        // Show  pages.
        return count;
    }
}