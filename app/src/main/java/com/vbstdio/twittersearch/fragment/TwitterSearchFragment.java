package com.vbstdio.twittersearch.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.astuetz.PagerSlidingTabStrip;
import com.vbstdio.twittersearch.R;
import com.vbstdio.twittersearch.dialog.dom.EmbeddedFragmentData;
import com.vbstdio.twittersearch.fragment.adapter.TwitterSearchPageAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 23/8/15.
 */
public class TwitterSearchFragment extends BaseFragment {

    private ViewPager twitterSearchViewPager;
    private TwitterSearchPageAdapter twitterSearchViewPageAdapter;
    private PagerSlidingTabStrip slidingTabs;
    private List<EmbeddedFragmentData> embeddedFragmentDataList;

    public static TwitterSearchFragment newInstance() {
        TwitterSearchFragment fragment = new TwitterSearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Twitter Search");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_twitter_search, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupView(view);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        final int id = view.getId();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        getNetworkManager().cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onErrorResponse(Request request, VolleyError error) {
        super.onErrorResponse(request, error);
    }

    @Override
    public void onResponse(Request<JSONObject> request, JSONObject response) {
        super.onResponse(request, response);

        if (response != null) {
            if (response.optInt("responseCode") == 1000) {

            }
        }
    }

    private void setupView(View view) {
        embeddedFragmentDataList = new ArrayList();

        addFragmentToList(TwitterUserSearchFragment.newInstance(), "User Search");
        addFragmentToList(TwitterHashTagSearchFragment.newInstance(), "# Search");
        addFragmentToList(TwitterStringSearchFragment.newInstance(), "String Search");

        twitterSearchViewPager = (ViewPager) view.findViewById(R.id.twitterSearchViewPager);
        twitterSearchViewPageAdapter = new TwitterSearchPageAdapter(getChildFragmentManager(), getActivity(), embeddedFragmentDataList);
        twitterSearchViewPager.setAdapter(twitterSearchViewPageAdapter);
        
        slidingTabs = (PagerSlidingTabStrip) view.findViewById(R.id.twitterSearchViewPagerTab);
        slidingTabs.setShouldExpand(true);
        slidingTabs.setTextColorResource(android.R.color.white);
        slidingTabs.setIndicatorColorResource(R.color.menuCta);
        slidingTabs.setIndicatorHeight(slidingTabs.getIndicatorHeight());
        slidingTabs.setUnderlineHeight(0);

        slidingTabs.setViewPager(twitterSearchViewPager);
    }

    private void addFragmentToList(BaseFragment fragment, String fragmentTitle) {
        EmbeddedFragmentData fragmentData = new EmbeddedFragmentData();

        fragmentData.setBaseFragment(fragment);
        fragmentData.setTitle(fragmentTitle);

        embeddedFragmentDataList.add(fragmentData);
    }
}
