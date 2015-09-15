package com.vbstudio.twittersearch.materialDrawer;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.vbstudio.twittersearch.R;

/**
 * Created by vaibhav on 7/9/15.
 */
public class SearchItMaterialDrawerItems {
    private static int selectedColor = R.color.transparent;
    private static int textColor = R.color.otherCta;
    private static int highlightTextColor = R.color.menuCta;
    private static int iconColor = R.color.lightGrey;
    private static int highlightIconColor = R.color.otherCta;


    public static PrimaryDrawerItem twitter = new PrimaryDrawerItem()
            .withName(R.string.item_twitter)
            .withSelectedColorRes(selectedColor)
            .withIcon(FontAwesome.Icon.faw_twitter)
            .withIconColorRes(iconColor)
            .withSelectedIconColorRes(highlightIconColor)
            .withTextColorRes(textColor)
            .withSelectedTextColorRes(highlightTextColor);

    public static PrimaryDrawerItem movies = new PrimaryDrawerItem()
            .withName(R.string.item_movies)
            .withSelectedColorRes(selectedColor)
            .withIcon(FontAwesome.Icon.faw_film)
            .withIconColorRes(iconColor)
            .withSelectedIconColorRes(highlightIconColor)
            .withTextColorRes(textColor)
            .withSelectedTextColorRes(highlightTextColor);

    public static PrimaryDrawerItem myProfile = new PrimaryDrawerItem()
            .withName(R.string.item_my_profile)
            .withSelectedColorRes(selectedColor)
            .withIcon(FontAwesome.Icon.faw_user_secret)
            .withIconColorRes(iconColor)
            .withSelectedIconColorRes(highlightIconColor)
            .withTextColorRes(textColor)
            .withSelectedTextColorRes(highlightTextColor);

    public static PrimaryDrawerItem logout = new PrimaryDrawerItem()
            .withName(R.string.item_logout)
            .withSelectedColorRes(selectedColor)
            .withIcon(FontAwesome.Icon.faw_sign_out)
            .withIconColorRes(iconColor)
            .withSelectedIconColorRes(highlightIconColor)
            .withTextColorRes(textColor)
            .withSelectedTextColorRes(highlightTextColor);
}
