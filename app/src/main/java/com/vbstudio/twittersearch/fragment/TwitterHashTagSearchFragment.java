package com.vbstudio.twittersearch.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.Twitter.TwitterSingleton;
import com.vbstudio.twittersearch.fragment.adapter.GenericSearchResultRecyclerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static com.vbstudio.twittersearch.utils.StringUtils.extractValidString;
import static com.vbstudio.twittersearch.utils.StringUtils.isValidString;
import static com.vbstudio.twittersearch.utils.UIUtils.hideKeyboard;

/**
 * Created by vaibhav on 23/8/15.
 */
public class TwitterHashTagSearchFragment extends BaseFragment implements TextView.OnEditorActionListener {

    long lastID = Long.MAX_VALUE;

    private RecyclerView tagsearchResultRecyclerView;
    private RecyclerView.Adapter tagsearchResultListAdapter;
    private RecyclerView.LayoutManager tagsearchResultRecyclerLayoutManager;

    private AsyncTask searchUserTask;
    private ArrayList<Status> statusResultList = new ArrayList<Status>();

    public static TwitterHashTagSearchFragment newInstance() {
        TwitterHashTagSearchFragment fragment = new TwitterHashTagSearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_twitter_tag_search, null);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
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
    public boolean onEditorAction(final TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchUser(textView);
            return true;
        }
        return false;
    }

    @Override
    public void onErrorResponse(Request request, VolleyError error) {
        super.onErrorResponse(request, error);
    }

    @Override
    public void onResponse(Request<JSONObject> request, JSONObject response) {
        super.onResponse(request, response);

        if (response != null) {

        }
    }

    private void setupView(View view) {
        statusResultList = new ArrayList<Status>();

        tagsearchResultRecyclerView = (RecyclerView) view.findViewById(R.id.tagsearchResultRecyclerView);
        tagsearchResultRecyclerLayoutManager = new LinearLayoutManager(getActivity());
        tagsearchResultRecyclerView.setLayoutManager(tagsearchResultRecyclerLayoutManager);
        tagsearchResultListAdapter = new GenericSearchResultRecyclerAdapter(tagsearchResultRecyclerView, statusResultList, getActivity(), this);
        tagsearchResultRecyclerView.swapAdapter(tagsearchResultListAdapter, true);

        EditText edtTagToBeSearched = ((EditText) view.findViewById(R.id.edtTagToBeSearched));
        edtTagToBeSearched.setOnEditorActionListener(this);
    }


    private void searchUser(final TextView textView) {
        hideKeyboard(getActivity());
        final String tagToBeSearched = extractValidString(getTextFromView(textView.getId()));

        if (isValidString(tagToBeSearched)) {
            showLoadingIndicator();

            searchUserTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {

                    makeTheApiCall(tagToBeSearched);

                    return null;
                }
            }.execute();
        } else {
            Toast.makeText(getActivity(), "Enter a valid tag to be searched", Toast.LENGTH_LONG).show();
        }
    }

    private void makeTheApiCall(String tagToBeSearched) {
        statusResultList = new ArrayList<>();

        try {
            Twitter twitter = TwitterSingleton.getInstance(getActivity());
            Query query = new Query("#" + tagToBeSearched);
            QueryResult result;
            int numberOfTweets = 100;
            int searchInterval = 10;
            int sizeInPreviousSearch = statusResultList.size();
            int retries = 0;
            int maxRetries = 5;

            while (statusResultList.size() < numberOfTweets) {
                query.setCount(Math.min(searchInterval, numberOfTweets - statusResultList.size()));
                result = twitter.search(query);
                statusResultList.addAll(result.getTweets());

                Log.i(BaseFragment.LOG_TAG, "SEARCH RESULT FOR #TAG SIZE: " + statusResultList.size());

                for (Status status : statusResultList) {
                    if (status.getId() < lastID) {
                        lastID = status.getId();
                    }
                }

                if (sizeInPreviousSearch == statusResultList.size()) {
                    if(retries == maxRetries) {
                        break;
                    }
                    retries++;
                } else {
                    retries = 0;
                }
                sizeInPreviousSearch = statusResultList.size();

                query.setMaxId(lastID - 1);
            }

            endAsyncTask("", false);
        } catch (TwitterException exception) {
            exception.printStackTrace();
            handleExcpetionInSearch(exception);
        }

    }

    private void handleExcpetionInSearch(TwitterException exception) {
        String message = "";

        if (exception.isCausedByNetworkIssue()) {
            message = "Verify your network";
        } else if (exception.getStatusCode() == 404) {
            statusResultList = new ArrayList<Status>();
        }
        endAsyncTask(message, true);
    }

    private void endAsyncTask(final String message, final Boolean isError) {
        searchUserTask.cancel(true);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoadingIndicator();
                addResultsToAdapter();

                if (isValidString(message)) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addResultsToAdapter() {
        if(statusResultList.size() == 0) {
            Toast.makeText(getActivity(), "No results found", Toast.LENGTH_LONG).show();
        }
        tagsearchResultListAdapter = new GenericSearchResultRecyclerAdapter(tagsearchResultRecyclerView, statusResultList, getActivity(), this);
        tagsearchResultRecyclerView.swapAdapter(tagsearchResultListAdapter, true);
    }
}
