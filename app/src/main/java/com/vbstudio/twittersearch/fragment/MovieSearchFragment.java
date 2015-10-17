package com.vbstudio.twittersearch.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vbstudio.twittersearch.Constants.ConstantKeys;
import com.vbstudio.twittersearch.R;

import org.json.JSONObject;

/**
 * Created by vaibhav on 23/8/15.
 */
public class MovieSearchFragment extends BaseFragment {


    public static MovieSearchFragment newInstance() {
        MovieSearchFragment fragment = new MovieSearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_movie_search, null);
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

        searchForMovie();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
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

    @Override
    public JSONObject processServerGETResponse(Request<String> request, String response) {
        return super.processServerGETResponse(request, response);
    }

    private void setupView(View view) {

    }

    private void searchForMovie() {
        showLoadingIndicator();

        String strGetMovieAutoSuggest = "http://sg.media-imdb.com/suggests/f/foo.json";

        setNetworkManager(strGetMovieAutoSuggest).jsonGetRequest(ConstantKeys.MOVIE_AUTO_SUGGEST, strGetMovieAutoSuggest, new Response.Listener<String>() {
            @Override
            public void onResponse(Request<String> request, String response) {
                processSuggestions(processServerGETResponse(request, response));
            }
        }, this, false);
    }

    private void processSuggestions(JSONObject jsonObject) {

    }
}