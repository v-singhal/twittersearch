package com.vbstudio.twittersearch.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.fragment.BaseFragment;
import com.vbstudio.twittersearch.network.AnimatedNetworkImageView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by vaibhav on 17/4/15.
 */
public class GenericSearchResultRecyclerAdapter extends RecyclerView.Adapter<GenericSearchResultRecyclerAdapter.GenericSearchResultViewHolder> implements View.OnClickListener {

    private List<Status> resultList;
    private Context context;
    private BaseFragment baseFragment;
    private ViewGroup upcomingSessionRecyclerView;
    private List<View> parentItemViewList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public GenericSearchResultRecyclerAdapter(RecyclerView upcomingSessionRecyclerView, ArrayList<Status> resultList, Context context, BaseFragment baseFragment) {
        this.resultList = resultList;
        this.context = context;
        this.baseFragment = baseFragment;
        this.upcomingSessionRecyclerView = upcomingSessionRecyclerView;
        this.parentItemViewList = new ArrayList<View>();
    }

    @Override
    public GenericSearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_generic_result, parent, false);
        GenericSearchResultViewHolder vh = new GenericSearchResultViewHolder(v, context, baseFragment);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenericSearchResultViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String sessionType = "";
        String resultValueUsername = "@" + resultList.get(position).getUser().getScreenName();
        String resultValue = resultList.get(position).getText();
        MediaEntity[] imageList = resultList.get(position).getMediaEntities();

        holder.txtSearchValueUser.setText(resultValueUsername);
        holder.txtSearchValue.setText(resultValue);
        holder.imgTweetImage.setImageBitmap(null);

        try {
            if (imageList.length > 0) {
                holder.imgTweetImage.setVisibility(View.VISIBLE);
                holder.imgTweetImage.setImageUrl(imageList[0].getMediaURL(), baseFragment.getImageLoader());
            } else {
                holder.imgTweetImage.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.imgTweetImage.setVisibility(View.GONE);
        }

        holder.txtSearchValue.setOnClickListener(this);
        setViewPosition((View) holder.txtSearchValue, position);

        parentItemViewList.add(holder.parentItemView);

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

    }

    private int getViewPosition(View view) {
        return (int) view.getTag(view.getId());
    }

    private void setViewPosition(View view, int position) {
        view.setTag(view.getId(), position);
    }

    /**
     * ******************************************
     */

    public static class GenericSearchResultViewHolder extends RecyclerView.ViewHolder {

        public View parentItemView;
        public TextView txtSearchValueUser;
        public TextView txtSearchValue;
        public AnimatedNetworkImageView imgTweetImage;

        public GenericSearchResultViewHolder(View view, Context context, BaseFragment baseFragment) {
            super(view);

            parentItemView = view;

            txtSearchValueUser = (TextView) view.findViewById(R.id.txtSearchValueUser);
            txtSearchValue = (TextView) view.findViewById(R.id.txtSearchValue);
            imgTweetImage = (AnimatedNetworkImageView) view.findViewById(R.id.imgTweetImage);
        }
    }
}