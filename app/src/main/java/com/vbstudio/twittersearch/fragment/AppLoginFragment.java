package com.vbstudio.twittersearch.fragment;


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
import com.vbstudio.twittersearch.Constants.ConstanKeys;
import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.TwitterLoginActivity;
import com.vbstudio.twittersearch.preferences.SearchItPreferences;

import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static com.vbstudio.twittersearch.utils.StringUtils.isValidString;

/**
 * Created by vaibhav on 23/8/15.
 */
public class AppLoginFragment extends BaseFragment {

    private Twitter twitter;
    private RequestToken requestToken;

    public static AppLoginFragment newInstance() {
        AppLoginFragment fragment = new AppLoginFragment();

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
            final Intent intent = new Intent(getActivity(), TwitterLoginActivity.class);
            intent.putExtra(TwitterLoginActivity.EXTRA_URL, requestToken.getAuthenticationURL());
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

            saveInfoInSharedPreferences(accessToken);
            openTwitterSearchFragment();

        } catch (Exception e) {
            Log.e(BaseFragment.LOG_TAG, "Twitter Login Failed: " + e.getMessage());
        }
        hideLoadingIndicator();
    }

    private void saveInfoInSharedPreferences(AccessToken accessToken) {
        try {
            String accessTokenForRequests = accessToken.getToken();
            String accessTokenSecretForRequests = accessToken.getTokenSecret();
            long userId = accessToken.getUserId();
            User user = twitter.showUser(userId);
            String userImageUrl = user.getOriginalProfileImageURL();
            String userBackgroundImageUrl = user.getProfileBannerURL();
            String username = user.getName();
            String userHandle = user.getScreenName();

            if(!isValidString(userBackgroundImageUrl)) {
                userBackgroundImageUrl = user.getProfileBackgroundImageURL();
            }

            Log.i(BaseFragment.LOG_TAG, "accessTokenForRequests "  + accessTokenForRequests);
            Log.i(BaseFragment.LOG_TAG, "accessTokenSecretForRequests "  + accessTokenSecretForRequests);
            Log.i(BaseFragment.LOG_TAG, "userId "  + String.valueOf(userId));
            Log.i(BaseFragment.LOG_TAG, "userImageUrl "  + userImageUrl);
            Log.i(BaseFragment.LOG_TAG, "userBackgroundImageUrl "  + userBackgroundImageUrl);
            Log.i(BaseFragment.LOG_TAG, "username "  + username);
            Log.i(BaseFragment.LOG_TAG, "userHandle "  + userHandle);

            SearchItPreferences.saveUserLoginState(getActivity(), true);
            SearchItPreferences.saveTwitterToken(getActivity(), accessTokenForRequests);
            SearchItPreferences.saveTwitterTokenSecret(getActivity(), accessTokenSecretForRequests);
            SearchItPreferences.saveUserProfileImgUrl(getActivity(), userImageUrl);
            SearchItPreferences.saveUserProfileBackgroundImgUrl(getActivity(), userBackgroundImageUrl);
            SearchItPreferences.saveUserId(getActivity(), userId);
            SearchItPreferences.saveUserName(getActivity(), username);
            SearchItPreferences.saveUserHandle(getActivity(), userHandle);
            //SearchItPreferences.saveOauthToken(getActivity(), accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTwitterSearchFragment() {
        replaceStack(getActivity(), TwitterSearchFragment.newInstance());
    }
}
