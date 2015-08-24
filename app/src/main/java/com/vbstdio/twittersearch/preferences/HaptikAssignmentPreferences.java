package com.vbstdio.twittersearch.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class HaptikAssignmentPreferences {

    public static final String IS_LOGGED_IN = "isMainPaused";
    public static final String TWITTER_TOKEN = "twitterToken";
    public static final String TWITTER_TOKEN_SECRET = "twitterTokenSecret";
    public static final String USERID = "userId";
    public static final String USERNAME = "username";
    public static final String OAUTH_TOKEN = "oauthToken";

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences("orobindPreferences", Context.MODE_PRIVATE);
	}
	
	public static void clearAll(Context context) {
		getPreferences(context).edit().clear().commit();
	}

    public static Boolean getUserLoginState(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public static void saveUserLoginState(Context context, Boolean isMainPaused) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, isMainPaused).commit();

    }

    public static String getTwitterToken(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getString(TWITTER_TOKEN, "");
    }

    public static void saveTwitterToken(Context context, String twitterToken) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putString(TWITTER_TOKEN, twitterToken).commit();

    }

    public static String getTwitterTokenSecret(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getString(TWITTER_TOKEN_SECRET, "");
    }

    public static void saveTwitterTokenSecret(Context context, String twitterTokenSecret) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putString(TWITTER_TOKEN_SECRET, twitterTokenSecret).commit();

    }

    public static long getUserId(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getLong(USERID, 0);
    }

    public static void saveUserId(Context context, long userId) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putLong(USERID, userId).commit();

    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getString(USERNAME, "");
    }

    public static void saveUserName(Context context, String username) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putString(USERNAME, username).commit();

    }

    public static String getOauthToken(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getString(OAUTH_TOKEN, "");
    }

    public static void saveOauthToken(Context context, String username) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putString(OAUTH_TOKEN, username).commit();

    }
}
