package com.vbstdio.twittersearch.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.vbstdio.twittersearch.Constants.ConstanKeys;
import com.vbstdio.twittersearch.R;
import com.vbstdio.twittersearch.WebViewActivity;
import com.vbstdio.twittersearch.preferences.HaptikAssignmentPreferences;

import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by vaibhav on 23/8/15.
 */
public class LoginFragment extends BaseFragment {

    private Twitter twitter;
    private RequestToken requestToken;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Expert Found");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_login, null);
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

        if (id == R.id.btnTwitterLogin) {
            initiateTwitterLogin();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        hideToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        hideLoadingIndicator();

        if (resultCode == Activity.RESULT_OK) {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {

                    finalizeLogin(data);

                    return null;
                }
            }.execute();
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        view.findViewById(R.id.btnTwitterLogin).setOnClickListener(this);
    }

    private void initiateTwitterLogin() {
        showLoadingIndicator();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                getTwitterOthToken();

                return null;
            }
        }.execute();
    }

    private void getTwitterOthToken() {

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(ConstanKeys.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(ConstanKeys.TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();

        try {
            requestToken = twitter.getOAuthRequestToken(ConstanKeys.TWITTER_CALLBACK_URL);
            final Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
            startActivityForResult(intent, 101);

        } catch (TwitterException e) {
            hideLoadingIndicator();
            e.printStackTrace();
        }
    }

    private void finalizeLogin(Intent data) {
        String verifier = data.getExtras().getString(ConstanKeys.URL_TWITTER_OAUTH_VERIFIER);
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

            saveInfoInSharedPrefernces(accessToken);
            openTwitterSearchFragment();
            enableLogoutButton();

        } catch (Exception e) {
            Log.e(BaseFragment.LOG_TAG, "Twitter Login Failed: " + e.getMessage());
        }
    }

    private void saveInfoInSharedPrefernces(AccessToken accessToken) {
        try {
            String accessTokenForRequests = accessToken.getToken();
            String accessTokenSecretForRequests = accessToken.getTokenSecret();
            long userId = accessToken.getUserId();
            User user = twitter.showUser(userId);
            String username = user.getName();

            Log.i(BaseFragment.LOG_TAG, "accessTokenForRequests "  + accessTokenForRequests);
            Log.i(BaseFragment.LOG_TAG, "accessTokenSecretForRequests "  + accessTokenSecretForRequests);
            Log.i(BaseFragment.LOG_TAG, "userId "  + String.valueOf(userId));
            Log.i(BaseFragment.LOG_TAG, "username "  + username);

            HaptikAssignmentPreferences.saveUserLoginState(getActivity(), true);
            HaptikAssignmentPreferences.saveTwitterToken(getActivity(), accessTokenForRequests);
            HaptikAssignmentPreferences.saveTwitterTokenSecret(getActivity(), accessTokenSecretForRequests);
            HaptikAssignmentPreferences.saveUserId(getActivity(), userId);
            HaptikAssignmentPreferences.saveUserName(getActivity(), username);
            //HaptikAssignmentPreferences.saveOauthToken(getActivity(), accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTwitterSearchFragment() {
        replaceStack(getActivity(), TwitterSearchFragment.newInstance());
    }
}