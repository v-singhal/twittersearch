package com.vbstudio.twittersearch.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.network.AnimatedNetworkImageView;
import com.vbstudio.twittersearch.network.ImageRequestManager;
import com.vbstudio.twittersearch.network.NetworkManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import static com.vbstudio.twittersearch.utils.StringUtils.isValidString;
import static com.vbstudio.twittersearch.utils.UIUtils.animateFadeHide;
import static com.vbstudio.twittersearch.utils.UIUtils.animateFadeShow;
import static com.vbstudio.twittersearch.utils.UIUtils.animateImageAddition;
import static com.vbstudio.twittersearch.utils.UIUtils.hideKeyboard;

public class BaseFragment extends DialogFragment implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    public final static String LOG_TAG = "SEARCH_IT";
    public final static String TWITTER_API_BASE_URL = "https://api.twitter.com/1.1";

    private String title;
    private NetworkManager networkManager;
    private ImageRequestManager imageRequestManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.networkManager = NetworkManager.newInstance(getActivity(), TWITTER_API_BASE_URL);
        this.imageRequestManager = ImageRequestManager.getInstance(getActivity());

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();

        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        return;
    }

    @Override
    public void onResume() {
        super.onResume();

        hideKeyboard(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        getNetworkManager().cancel();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideLoadingIndicator();

        getActivity().invalidateOptionsMenu();

        ((ActionBarActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Log.i("", "NetworkManager is null");
        // networkManager = null;
    }

    @Override
    public void onErrorResponse(Request request, VolleyError error) {
        hideLoadingIndicator();
        Log.e(BaseFragment.LOG_TAG, "ERROR RESPONSE on: " + request.getIdentifier());
        Log.e(BaseFragment.LOG_TAG, "ERROR RESPONSE: " + error.toString());

        /*********PRINT HTTP ERROR CODES*********/
        if (error.networkResponse != null) {
            Log.i(BaseFragment.LOG_TAG, "HTTP ERROR CODE: " + error.networkResponse.statusCode);
        }
        /****************************************/

        if (error.getCause() != null && error.getCause() instanceof UnknownHostException) {
            Toast.makeText(getActivity().getApplicationContext(), "Internet unavailable", Toast.LENGTH_LONG).show();
            return;
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getActivity().getApplicationContext(), "Internet unavailable", Toast.LENGTH_LONG).show();
            return;
        } else if (error instanceof TimeoutError) {
            Toast.makeText(getActivity().getApplicationContext(), "Your request has timed out. Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            return;
        } else if (error instanceof ServerError) {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == 403) {
                    Toast.makeText(getActivity(), "Authentication Error", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Some error occurred. Please try again", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @Override
    public void onResponse(Request<JSONObject> request, JSONObject response) {
        hideLoadingIndicator();
        Log.e(BaseFragment.LOG_TAG, "RESPONSE on: " + request.getIdentifier());
        Log.e(BaseFragment.LOG_TAG, "RESPONSE: " + response.toString());
    }

    public void hideLoadingIndicator() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animateFadeHide(getActivity().findViewById(R.id.pageLoadingIndicator));
                }
            });
        }
    }

    public void showLoadingIndicator() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animateFadeShow(getActivity().findViewById(R.id.pageLoadingIndicator));
                }
            });
        }
    }

    public static void addToBackStack(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((ActionBarActivity) context).getSupportFragmentManager();

        Log.i(BaseFragment.LOG_TAG, "FM SIZE IN ADD BEFORE: " + fragmentManager.getBackStackEntryCount());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();

        Log.i(BaseFragment.LOG_TAG, "FM SIZE IN ADD AFTER: " + fragmentManager.getBackStackEntryCount());
    }

    public static void replaceStack(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((ActionBarActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(R.id.container, fragment).commit();
    }

    public static void openAniamtedDialog(Dialog dialog) {
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();
    }

    protected void hideToolbar() {
        if (getActivity() != null) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    protected String getTextFromView(int id) {
        return ((TextView) getView().findViewById(id)).getText().toString();
    }

    protected void addDataForValidString(String value, int viewId) {
        Log.i(BaseFragment.LOG_TAG, value);
        if (!isValidString(value)) {
            value = "-";
        }
        ((TextView) getView().findViewById(viewId)).setText(value);

    }

    public static Typeface customTypefaceFromBase(Context context) {
        return Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.custom_font_path));
    }

    public void addImageToAnimatedNetworkImageView(String imageUrl, int imgViewId) {
        //((AnimatedNetworkImageView) getView().findViewById(imgViewId)).setImageBitmap(null);
        if (isValidString(imageUrl)) {
            ((AnimatedNetworkImageView) getView().findViewById(imgViewId)).setImageUrl(imageUrl, getImageRequestManager().getImageLoader());
        }
    }

    public void addImageToImageView(final Activity activity, final String imageUrl, int imageViewId) {
        final ImageView imageView = (ImageView) getView().findViewById(imageViewId);
        //imageView.setImageBitmap(null);

        if (isValidString(imageUrl)) {
            new AsyncTask<String, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    return getBitmapFromUrl(imageUrl);
                }

                @Override
                protected void onPostExecute(final Bitmap imageBitmap) {
                    if(imageBitmap != null) {
                        addBitmapToView(activity, imageBitmap, imageView);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                }
            }.execute();
        }
    }

    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addBitmapToView(Activity activity, final Bitmap imageBitmap, final ImageView imageView) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animateImageAddition(imageBitmap, imageView);
            }
        });
    }

    /**
     * *******************************
     */

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public ImageRequestManager getImageRequestManager() {
        return imageRequestManager;
    }

    public void setImageRequestManager(ImageRequestManager imageRequestManager) {
        this.imageRequestManager = imageRequestManager;
    }

    public ImageLoader getImageLoader() {
        return getImageRequestManager().getImageLoader();
    }
}
