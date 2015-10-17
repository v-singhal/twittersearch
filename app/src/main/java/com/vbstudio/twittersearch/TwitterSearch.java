package com.vbstudio.twittersearch;

import android.app.Application;
import android.util.Log;

import com.vbstudio.twittersearch.fragment.BaseFragment;
import com.vbstudio.twittersearch.preferences.SearchItPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class TwitterSearch extends Application {

    public TwitterSearch() {
        super();
    }

    @Override
    public void onCreate () {
        super.onCreate();
        /***********ADD CUSTOM FONTS TO APP**************/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getResources().getString(R.string.custom_font_path))
                .setFontAttrId(R.attr.fontPath)
                .build());
        /***********************************************/
    }

    @Override
    public void onTrimMemory(final int level) {
        super.onTrimMemory(level);

        if (!SearchItPreferences.getAppBackgroundPauseState(getApplicationContext())) {
            Log.i(BaseFragment.LOG_TAG, "APP WENT TO BACKGROUND");

            SearchItPreferences.saveAppBackgroundPauseState(getApplicationContext(), true);
            //BaseFragment.logIntercomEvent(IntercomEvents.INTERCOM_EVENT_APPLICATION_BACKGROUND, null);
        }
    }

}