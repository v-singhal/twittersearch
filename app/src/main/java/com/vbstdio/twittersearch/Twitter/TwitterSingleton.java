package com.vbstdio.twittersearch.Twitter;

import android.content.Context;

import com.vbstdio.twittersearch.Constants.ConstanKeys;
import com.vbstdio.twittersearch.preferences.HaptikAssignmentPreferences;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by vaibhav on 23/8/15.
 */
public class TwitterSingleton {

    private static Twitter twitter;

    private TwitterSingleton() {

    }

    public static Twitter getInstance(Context context) {
        if(twitter == null) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(ConstanKeys.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(ConstanKeys.TWITTER_CONSUMER_SECRET);
            builder.setOAuthAccessToken(HaptikAssignmentPreferences.getTwitterToken(context));
            builder.setOAuthAccessTokenSecret(HaptikAssignmentPreferences.getTwitterTokenSecret(context));
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();
        }

        return twitter;
    }
}
