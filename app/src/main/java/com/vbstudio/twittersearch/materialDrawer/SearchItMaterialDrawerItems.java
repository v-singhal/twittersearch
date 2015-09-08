package com.vbstudio.twittersearch.materialDrawer;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.vbstudio.twittersearch.R;

/**
 * Created by vaibhav on 7/9/15.
 */
public class SearchItMaterialDrawerItems {
    private static int textColor = R.color.otherCta;
    private static int highlightTextColor = R.color.mainCta;


    public static PrimaryDrawerItem twitter = new PrimaryDrawerItem()
            .withName(R.string.item_twitter)
            .withTextColorRes(textColor)
            .withSelectedColorRes(R.color.transparent)
            .withSelectedTextColorRes(textColor);

    public static PrimaryDrawerItem movies = new PrimaryDrawerItem()
            .withName(R.string.item_movies)
            .withTextColorRes(textColor)
            .withSelectedColorRes(R.color.transparent)
            .withSelectedTextColorRes(textColor);

    public static PrimaryDrawerItem myProfile = new PrimaryDrawerItem()
            .withName(R.string.item_my_profile)
            .withTextColorRes(textColor)
            .withSelectedColorRes(R.color.transparent)
            .withSelectedTextColorRes(textColor);

    public static PrimaryDrawerItem logout = new PrimaryDrawerItem()
            .withName(R.string.item_logout)
            .withTextColorRes(textColor)
            .withSelectedColorRes(R.color.transparent)
            .withSelectedTextColorRes(textColor);
}
