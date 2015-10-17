package com.vbstudio.twittersearch.fragment.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vbstudio.twittersearch.dom.EmbeddedFragmentData;
import com.vbstudio.twittersearch.fragment.BaseFragment;

import java.util.List;

/**
 * Created by vaibhav on 23/8/15.
 */
public class TwitterSearchPageAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<EmbeddedFragmentData> embeddedFragmentDataList;

    public TwitterSearchPageAdapter(FragmentManager fm, Context context, List<EmbeddedFragmentData> embeddedFragmentDataList) {
        super(fm);
        this.context = context;
        this.embeddedFragmentDataList = embeddedFragmentDataList;
    }

    @Override
    public BaseFragment getItem(int position) {

        return embeddedFragmentDataList.get(position).getBaseFragment();
    }

    @Override
    public int getCount() {
        return  embeddedFragmentDataList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return embeddedFragmentDataList.get(position).getTitle();
    }
}