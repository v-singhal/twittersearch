package com.vbstudio.twittersearch.Constants;

/**
 * Created by vaibhav on 23/8/15.
 */
public class ConstantKeys {

    public static final String TWITTER_CONSUMER_KEY = "fkL7ymH1gAVKJ7RX2cuJTzmzO";
    public static final String TWITTER_CONSUMER_SECRET = "SVPobxi9RlhAWs0tlNXNIJcPksP8a0dxHzkRvCAJYuX5omp5Zr";

    // Preference Constants
    public static final String PREFERENCE_NAME = "twitter_oauth";
    public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    public static final String TWITTER_CALLBACK_URL = "http://twittersearch-app.com";

    // Twitter oauth urls
    public static final String URL_TWITTER_AUTH = "auth_url";
    public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    public static final String URL_TWITTER_OATH_FAIL = "https://api.twitter.com/login/error?username_or_email=";

    //Movie Search Response Key
    public static final String MOVIE_RESPONSE_V = "v";
    public static final String MOVIE_RESPONSE_QUERY = "q";
    public static final String MOVIE_RESPONSE_BASIC_ARRAY = "d";
    public static final String MOVIE_RESPONSE_BASIC_ID = "id";
    public static final String MOVIE_RESPONSE_BASIC_NAME = "l";
    public static final String MOVIE_RESPONSE_BASIC_CAST = "s";
    public static final String MOVIE_RESPONSE_BASIC_TYPE = "q";
    public static final String MOVIE_RESPONSE_BASIC_YEAR = "y";
    public static final String MOVIE_RESPONSE_BASIC_IMAGE_DETAILS = "i";

    //Movie Search Key
    public static final int MOVIE_AUTO_SUGGEST = 5000;

}
