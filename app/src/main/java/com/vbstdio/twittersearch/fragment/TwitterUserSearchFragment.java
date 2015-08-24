package com.vbstdio.twittersearch.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
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
import com.vbstdio.twittersearch.R;
import com.vbstdio.twittersearch.Twitter.TwitterSingleton;

import org.json.JSONObject;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import static com.vbstdio.twittersearch.utils.StringUtils.extractValidString;
import static com.vbstdio.twittersearch.utils.StringUtils.isValidString;
import static com.vbstdio.twittersearch.utils.UIUtils.hideKeyboard;

/**
 * Created by vaibhav on 23/8/15.
 */
public class TwitterUserSearchFragment extends BaseFragment implements TextView.OnEditorActionListener {

    private AsyncTask searchUserTask;

    public static TwitterUserSearchFragment newInstance() {
        TwitterUserSearchFragment fragment = new TwitterUserSearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_twitter_user_search, null);
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
        getView().findViewById(R.id.userDetails).setVisibility(View.GONE);

        EditText userNameEditText = ((EditText) view.findViewById(R.id.edtUserToBeSearched));
        userNameEditText.setOnEditorActionListener(this);;

    }



    private void searchUser(final TextView textView) {
        hideKeyboard(getActivity());
        final String username = extractValidString(getTextFromView(textView.getId()));

        if(isValidString(username)) {
            showLoadingIndicator();

            searchUserTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {

                    makeTheApiCall(username);

                    return null;
                }
            }.execute();
        }
        else {
            Toast.makeText(getActivity(), "Enter a valid user name to be searched", Toast.LENGTH_LONG).show();
        }
    }

    private void makeTheApiCall(String username) {

        try {
            Twitter twitter = TwitterSingleton.getInstance(getActivity());
            ResponseList<User> userResponseList = twitter.lookupUsers(username);

            Log.i(BaseFragment.LOG_TAG, "USER SEARCH RESULT: " + userResponseList.size());
            addUserInfoToView(userResponseList.get(0));

            endAsyncTask("", false);
        } catch (TwitterException exception) {
            exception.printStackTrace();
            handleExcpetionInSearch(exception);
        }
    }

    private void addUserInfoToView(final User user) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String name = user.getName();
                String screenName = user.getScreenName();
                String location = user.getLocation();
                String friendCount = String.valueOf(user.getFriendsCount());
                String description = user.getDescription();
                String userImageUrl = user.getBiggerProfileImageURL();

                getView().findViewById(R.id.userDetails).setVisibility(View.VISIBLE);

                addDataForValidString(name, R.id.txtUserName);
                addDataForValidString(screenName, R.id.txtUserScreenName);
                addDataForValidString(location, R.id.txtUserLocation);
                addDataForValidString(friendCount, R.id.txtUserFriendCount);
                addDataForValidString(description, R.id.txtUserDescription);
                addImageToView(userImageUrl, R.id.imgUserProfileImage);
            }
        });
    }

    private void handleExcpetionInSearch(TwitterException exception) {
        String message = "";

        if(exception.isCausedByNetworkIssue()) {
            message = "Verify your network";
        }
        else if(exception.getStatusCode() == 404) {
            message = "No results found";
        }
        endAsyncTask(message, true);
    }

    private void endAsyncTask(final String message, final Boolean isError) {
        searchUserTask.cancel(true);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoadingIndicator();

                if (isError) {
                    getView().findViewById(R.id.userDetails).setVisibility(View.GONE);
                }

                if (isValidString(message)) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
