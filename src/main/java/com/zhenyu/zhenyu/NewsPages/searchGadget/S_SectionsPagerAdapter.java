package com.zhenyu.zhenyu.NewsPages.searchGadget;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zhenyu.zhenyu.NewsPages.HFpages.HFfragement;

import java.util.ArrayList;

public class S_SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String[] TAB_CONTENT = new String[]{"收藏","历史"};
    private final Context mContext;
    private ArrayList<Integer> cur;
    private ArrayList<Integer>notuse;
    private int count;
    String keyword;
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public S_SectionsPagerAdapter(Context context, ArrayList<Integer>cur, ArrayList<Integer>notuse, FragmentManager fm,String key) {
        super(fm);
        count=cur.size();
        this.cur=cur;
        this.notuse=notuse;
        mContext = context;
        keyword=key;
    }
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return HFfragement.newInstance(position + 1,keyword);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_CONTENT[cur.get(position)];
    }

    @Override

    public int getCount() {
        // Show  pages.
        return 1;
    }

}