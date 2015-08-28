package com.vbstudio.twittersearch;

import android.app.Application;

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

}