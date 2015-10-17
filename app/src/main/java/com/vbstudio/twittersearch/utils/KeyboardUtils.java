package com.vbstudio.twittersearch.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by vaibhav on 17/10/15.
 */
public class KeyboardUtils {

    public static InputFilter getFilterToDisallowSpace() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isSpaceChar(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        return filter;
    }

    public static void showKeyboard(Context context) {
        if (context != null && ((Activity) context).getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInputFromInputMethod(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Context context) {
        if (context != null && ((Activity) context).getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        }
    }
}
