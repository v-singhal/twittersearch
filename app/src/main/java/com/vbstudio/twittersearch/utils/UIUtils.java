package com.vbstudio.twittersearch.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

/**
 * Created by vaibhav on 23/8/15.
 */
public class UIUtils {

    public static void hideKeyboard(Context context) {
        if(context != null && ((Activity) context).getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void animateFadeHide(View view) {
        //view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        view.animate().alpha(0f).setDuration(300);
        view.setVisibility(View.GONE);
    }

    public static void animateFadeShow(View view) {
        //view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        view.animate().alpha(1f).setDuration(300);
    }

    public static void animateImageAddition(Bitmap imageBitmap, ImageView imageView) {
        //imageView.setImageBitmap(null);
        imageView.setAlpha(0f);
        imageView.setImageBitmap(imageBitmap);

        imageView.animate().alpha(1f).setDuration(500);
    }
}
