package com.vbstudio.twittersearch.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vbstudio.twittersearch.Constants.ConstanKeys;
import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.TwitterLoginActivity;
import com.vbstudio.twittersearch.fragment.BaseFragment;

import static com.vbstudio.twittersearch.utils.UIUtils.animateFadeHide;
import static com.vbstudio.twittersearch.utils.UIUtils.animateFadeShow;

/**
 * Created by vaibhav on 10/4/15.
 */
public class TwitterLoginWebViewClient extends WebViewClient {

    private Context context;


    public TwitterLoginWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        animateFadeShow(((FragmentActivity) context).findViewById(R.id.pageLoadingIndicator));
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.contains(ConstanKeys.TWITTER_CALLBACK_URL)) {
            Uri uri = Uri.parse(url);

				/* Sending results back */
            String verifier = uri.getQueryParameter(ConstanKeys.URL_TWITTER_OAUTH_VERIFIER);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(ConstanKeys.URL_TWITTER_OAUTH_VERIFIER, verifier);
            ((TwitterLoginActivity) context).setResult(((TwitterLoginActivity) context).RESULT_OK, resultIntent);

				/* closing webview */
            ((TwitterLoginActivity) context).finish();
            return true;
        }
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        animateFadeHide(((FragmentActivity) context).findViewById(R.id.pageLoadingIndicator));

        Log.i(BaseFragment.LOG_TAG, "PAGE URL: " + url);
        Log.i(BaseFragment.LOG_TAG, "PAGE TITLE: " + view.getTitle());

        /*if (url.contains(ConstanKeys.TWITTER_CALLBACK_URL)) {
            Uri uri = Uri.parse(url);

            String verifier = uri.getQueryParameter(ConstanKeys.URL_TWITTER_OAUTH_VERIFIER);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(ConstanKeys.URL_TWITTER_OAUTH_VERIFIER, verifier);
            ((TwitterLoginActivity) context).setResult(((TwitterLoginActivity) context).RESULT_OK, resultIntent);

            ((TwitterLoginActivity) context).finish();
        }*/
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        //Toast.makeText(context, "ERROR CODE: " + errorCode, Toast.LENGTH_SHORT).show();
        //Log.e(BaseFragment.LOG_TAG, "WEBVIEW ERROR FROM PAGE: " + failingUrl);
        //Log.e(BaseFragment.LOG_TAG, "WEBVIEW ERROR DESCRIPTION: " + description);
    }
}
