package com.vbstudio.twittersearch.fragment.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.dom.MovieResponse.MovieBasicData;
import com.vbstudio.twittersearch.fragment.BaseFragment;
import com.vbstudio.twittersearch.network.AnimatedNetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 17/4/15.
 */
public class MovieSuggestionsRecyclerAdapter extends RecyclerView.Adapter<MovieSuggestionsRecyclerAdapter.MovieSuggestionsViewHolder> implements View.OnClickListener {

    private List<MovieBasicData> movieBasicDataList;
    private Context context;
    private BaseFragment baseFragment;
    private ViewGroup movieSuggestionsRecyclerView;
    private List<View> parentItemViewList;
    private View clickedView;
    private int clickedPositonForDetailedInfo;
    private Boolean isClickAllowed = true;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieSuggestionsRecyclerAdapter(RecyclerView movieSuggestionsRecyclerView, List<MovieBasicData> movieBasicDataList, Context context, BaseFragment baseFragment) {
        this.movieBasicDataList = movieBasicDataList;
        this.context = context;
        this.baseFragment = baseFragment;
        this.movieSuggestionsRecyclerView = movieSuggestionsRecyclerView;
        this.parentItemViewList = new ArrayList<View>();
    }

    @Override
    public MovieSuggestionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_movie_suggestion, parent, false);
        MovieSuggestionsViewHolder vh = new MovieSuggestionsViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(MovieSuggestionsViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        MovieBasicData movieSuggestionsData = movieBasicDataList.get(position);
        String movieName = movieSuggestionsData.getMovieResponseBasicName();
        String movieTypeWithYear = movieSuggestionsData.getMovieResponseBasicType() + " " +movieSuggestionsData.getMovieResponseBasicYear();
        String movieCast = movieSuggestionsData.getMovieResponseBasicCast();
        String movieImageUrl = movieSuggestionsData.getMovieBasicResponseImageData().getImageUrl();

        holder.txtMovieName.setText(movieName);
        holder.txtMovieTypeWithYear.setText(movieTypeWithYear);
        holder.txtMovieCast.setText(movieCast);
        holder.imgMoviePoster.setImageUrl(movieImageUrl, baseFragment.getImageLoader());

        /*******************************
        holder.accessSessionCancelButtonLayout.setOnClickListener(this);
        setViewPosition((View) holder.accessSessionCancelButtonLayout, position);
        /*******************************/

        parentItemViewList.add(holder.parentItemView);
    }

    @Override
    public int getItemCount() {
        return movieBasicDataList.size();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (isClickAllowed) {
            flipOnClickListener(view, this);
            int id = view.getId();
            Integer clickedPosition = getViewPosition(view);

        }

    }

    private int getViewPosition(View view) {
        return (int) view.getTag(view.getId());
    }

    private void setViewPosition(View view, int position) {
        view.setTag(view.getId(), position);
    }

    private void flipOnClickListener(final View view, final RecyclerView.Adapter recyclerAdapter) {
        view.setOnClickListener(null);
        isClickAllowed = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setOnClickListener((View.OnClickListener) recyclerAdapter);
                isClickAllowed = true;
            }
        }, 1000);
    }

    /**
     * ******************************************
     */

    public static class MovieSuggestionsViewHolder extends RecyclerView.ViewHolder {

        public View parentItemView;

        public TextView txtMovieName;
        public TextView txtMovieTypeWithYear;
        public TextView txtMovieCast;
        public AnimatedNetworkImageView imgMoviePoster;

        public MovieSuggestionsViewHolder(View view) {
            super(view);

            parentItemView = view;

            txtMovieName = (TextView) view.findViewById(R.id.txtMovieName);
            txtMovieTypeWithYear = (TextView) view.findViewById(R.id.txtMovieTypeWithYear);
            txtMovieCast = (TextView) view.findViewById(R.id.txtMovieCast);
            imgMoviePoster = (AnimatedNetworkImageView) view.findViewById(R.id.imgMoviePoster);
        }
    }
}