package com.vbstudio.twittersearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.vbstudio.twittersearch.dialog.LogoutConfirmationDialog;
import com.vbstudio.twittersearch.fragment.AppLoginFragment;
import com.vbstudio.twittersearch.fragment.BaseFragment;
import com.vbstudio.twittersearch.fragment.MovieSearchFragment;
import com.vbstudio.twittersearch.fragment.TwitterSearchFragment;
import com.vbstudio.twittersearch.materialDrawer.SearchItMaterialDrawerItems;
import com.vbstudio.twittersearch.network.AnimatedNetworkImageView;
import com.vbstudio.twittersearch.preferences.SearchItPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.vbstudio.twittersearch.utils.KeyboardUtils.hideKeyboard;
import static com.vbstudio.twittersearch.utils.StringUtils.extractValidString;
import static com.vbstudio.twittersearch.utils.StringUtils.isValidString;
import static com.vbstudio.twittersearch.utils.UIUtils.animateImageAddition;


public class MainActivity extends BaseActivity implements Drawer.OnDrawerListener, Drawer.OnDrawerItemClickListener {

    private ActionBarDrawerToggle drawerToggle;
    private Drawer materialDrawer;
    private Drawer.Result materialDrawerResult;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceListener;
    private boolean isSharedPreferenceListenerRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();
        initializeMaterialDrawer();
        openAptFragment();
    }

    private void openAptFragment() {
        BaseFragment fragment = new BaseFragment();

        if (SearchItPreferences.getUserLoginState(this)) {
            fragment = TwitterSearchFragment.newInstance();
        } else {
            fragment = AppLoginFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (materialDrawerResult != null && materialDrawerResult.isDrawerOpen()) {
            materialDrawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDrawerOpened(View view) {
        hideKeyboard(MainActivity.this);
    }

    @Override
    public void onDrawerClosed(View view) {

    }

    @Override
    public void onDrawerSlide(View view, float v) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        captureDrawerClicks(adapterView, view, i, l, iDrawerItem);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        return;
    }

    @Override
    public void onResume() {
        super.onResume();

        registerOrobindPreferenceListener();
    }

    private void registerOrobindPreferenceListener() {
        if (!isSharedPreferenceListenerRegistered) {
            registerSharedPreferenceListener();
        }
    }

    private void registerSharedPreferenceListener() {
        sharedPreferences = SearchItPreferences.getPreferences(this);
        sharedPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            /**
             * Called when a shared preference is changed, added, or removed. This
             * may be called even if a preference is set to its existing value.
             * <p/>
             * <p>This callback will be run on your main thread.
             *
             * @param sharedPreferences The {@link android.content.SharedPreferences} that received
             *                          the change.
             * @param key               The key of the preference that was changed, added, or
             */
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(SearchItPreferences.USERHANDLE)) {
                    Log.i(BaseFragment.LOG_TAG, "USER HANDLE IN MAIN " + key + ": " + sharedPreferences.getString(key, ""));
                    initializeMaterialDrawer();
                }
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceListener);
        isSharedPreferenceListenerRegistered = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceListener);
        isSharedPreferenceListenerRegistered = false;
    }

    private void configureToolbar() {
        super.configureToolbar((Toolbar) findViewById(R.id.appToolbar));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.closed_drawer);
    }


    private void initializeMaterialDrawer() {
        if (SearchItPreferences.getUserLoginState(this)) {
            materialDrawer = new Drawer();
            String username = extractValidString(SearchItPreferences.getUserName(this));
            String userHandle = "@" + SearchItPreferences.getUserHandle(this);
            String userImageUrl = SearchItPreferences.getUserProfileImgUrl(this);
            String userImageBackground = SearchItPreferences.getUserProfileBackgroundImgUrl(this);

            View materialDrawerHeader = getLayoutInflater().inflate(R.layout.material_drawer_custom_header, null);

            if (username.contains(" ") && username.split(" ").length > 0) {
                username = "Hi " + username.split(" ")[0];
            } else if (!isValidString(username)) {
                username = "Hi there";
            } else {
                username = "Hi " + username;
            }
            RelativeLayout headerDrawerLayout = (RelativeLayout) materialDrawerHeader.findViewById(R.id.header_drawer);
            AnimatedNetworkImageView headerBackGround = (AnimatedNetworkImageView) materialDrawerHeader.findViewById(R.id.header_drawer_background);
            ImageView userProfileImage = (ImageView) materialDrawerHeader.findViewById(R.id.userProfileImage);
            TextView headerName = (TextView) materialDrawerHeader.findViewById(R.id.header_drawer_name);
            TextView headerEmail = (TextView) materialDrawerHeader.findViewById(R.id.header_drawer_email);

            headerName.setText(username);
            if (!isValidString(userHandle)) {
                headerEmail.setVisibility(View.GONE);
            } else {
                headerEmail.setText(userHandle);
            }
            addImageToImageView(this, userImageBackground, headerBackGround);
            addImageToImageView(this, userImageUrl, userProfileImage);

            materialDrawer.withActivity(this);
            materialDrawer.withOnDrawerListener(this);
            materialDrawer.withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.backgorund_diamond_pattern));
            materialDrawer.withStickyHeader(materialDrawerHeader);
            materialDrawer.withHeaderClickable(false);
            materialDrawer.withToolbar(toolbar);
            materialDrawer.withActionBarDrawerToggle(true);
            materialDrawer.withActionBarDrawerToggleAnimated(true);
            materialDrawer.withActionBarDrawerToggle(drawerToggle);
            materialDrawer.withTranslucentStatusBar(false);
            materialDrawer = addProfileItemsToDrawer(materialDrawer);
            materialDrawer.withFireOnInitialOnClick(true);
            materialDrawerResult = materialDrawer.build();
            materialDrawerResult.keyboardSupportEnabled(this, false);
        }
    }

    private Drawer addProfileItemsToDrawer(Drawer materialDrawer) {
        materialDrawer.addDrawerItems(SearchItMaterialDrawerItems.twitter);
        materialDrawer.addDrawerItems(SearchItMaterialDrawerItems.movies);
        materialDrawer.addDrawerItems(SearchItMaterialDrawerItems.myProfile);
        materialDrawer.addDrawerItems(SearchItMaterialDrawerItems.logout);

        materialDrawer.withOnDrawerItemClickListener(this);
        materialDrawer.withSelectedItem(-1);
        return materialDrawer;
    }

    private void captureDrawerClicks(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
        if (drawerItem instanceof Nameable) {
            flipOnClickListener();
            materialDrawer.withSelectedItem(-1);

            String itemName = this.getString(((Nameable) drawerItem).getNameRes());
            if ((getString(R.string.item_twitter).equals(itemName))) {
                BaseFragment.addToBackStack(this, TwitterSearchFragment.newInstance());
            } else if ((getString(R.string.item_movies).equals(itemName))) {
                BaseFragment.addToBackStack(this, MovieSearchFragment.newInstance());
            } else if ((getString(R.string.item_my_profile).equals(itemName))) {

            } else if ((getString(R.string.item_logout).equals(itemName))) {
                openLogoutConfirmationDialog();
            }
        }
    }

    private void flipOnClickListener() {
        materialDrawer.withOnDrawerItemClickListener(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                materialDrawer.withOnDrawerItemClickListener(MainActivity.this);
            }
        }, 1000);
    }

    private Drawer inflateOptionsForPaidUser(Drawer materialDrawer) {

        return materialDrawer;
    }

    private void openLogoutConfirmationDialog() {
        LogoutConfirmationDialog logoutConfirmationDialog = new LogoutConfirmationDialog(this);
        BaseFragment.openAniamtedDialog(logoutConfirmationDialog);
    }

    public void performActionLogout() {
        DrawerLayout drawerLayout = materialDrawerResult.getDrawerLayout();

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        materialDrawerResult.removeAllItems();
        SearchItPreferences.clearAll(this);
        BaseFragment.replaceStack(this, AppLoginFragment.newInstance());
    }

    public void addImageToImageView(final Activity activity, final String imageUrl, final ImageView imageView) {
        //imageView.setImageBitmap(null);

        if (isValidString(imageUrl)) {
            new AsyncTask<String, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    return getBitmapFromUrl(imageUrl);
                }

                @Override
                protected void onPostExecute(final Bitmap imageBitmap) {
                    if (imageBitmap != null) {
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
}
