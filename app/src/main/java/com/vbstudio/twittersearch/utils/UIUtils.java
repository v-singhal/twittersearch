package com.vbstudio.twittersearch.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import com.vbstudio.twittersearch.R;

/**
 * Created by vaibhav on 23/8/15.
 */
public class UIUtils {

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics;
    }

    public static void animateFlipHide(View view) {
        animateFlipShow(view);
        view.animate().scaleY(0).alpha(0f).setDuration(200);
        view.setVisibility(View.GONE);
    }

    public static void animateFlipShow(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        view.setScaleY(0);
        view.animate().scaleY(1).alpha(1f).setDuration(200);
    }

    public static void animateScrollHide(Context context, View view) {
        Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        view.startAnimation(slide_up);
        view.setVisibility(View.GONE);
    }

    public static void animateScrollShow(Context context, View view) {
        Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        view.startAnimation(slide_up);
        view.setVisibility(View.VISIBLE);
    }

    public static void animateSlideDown(Context context, View view) {
        Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        slideDown.setDuration(500);
        view.startAnimation(slideDown);
        view.setVisibility(View.GONE);
    }

    public static void animateSlideUp(Context context, View view) {
        Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        slideUp.setDuration(500);
        view.startAnimation(slideUp);
        view.setVisibility(View.VISIBLE);
    }

    public static void animateFadeHide(View view) {
        //view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //view.animate().alpha(0f).setDuration(300);
        view.setVisibility(View.GONE);
    }

    public static void animateFadeShow(View view) {
        //view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        //view.animate().alpha(1f).setDuration(300);
    }

    public static void animateFadeHide(Context context, View view) {
        Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        view.startAnimation(animFadeOut);
        view.setVisibility(View.GONE);
    }

    public static void animateFadeShow(Context context, View view) {
        Animation animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        view.startAnimation(animFadeIn);
        view.setVisibility(View.VISIBLE);
    }

    public static void animateTextAddition(final Context context, final TextSwitcher textSwitcher, final int textViewGravity, String message) {
        textSwitcher.setInAnimation(context, R.anim.drop_bounce);
        textSwitcher.setOutAnimation(context, R.anim.slide_down);

        textSwitcher.setText(message);
    }

    public static void animateMoveViewOnY(View view, float distanceToMove) {

        view.animate().translationY(distanceToMove).setDuration(500);
    }

    public static void animateDropBounce(Context context, View view) {
        Animation animDropBounce = AnimationUtils.loadAnimation(context, R.anim.drop_bounce);

        view.startAnimation(animDropBounce);
        view.setVisibility(View.VISIBLE);
    }

    public static void animateShake(Context context, View view) {
        Animation animDropBounce = AnimationUtils.loadAnimation(context, R.anim.shake);

        view.startAnimation(animDropBounce);
        view.setVisibility(View.VISIBLE);
    }

    public static void animateImageAddition(Bitmap imageBitmap, ImageView imageView) {
        //imageView.setImageBitmap(null);
        imageView.setAlpha(0f);
        imageView.setImageBitmap(imageBitmap);

        imageView.animate().alpha(1f).setDuration(500);
    }
}
