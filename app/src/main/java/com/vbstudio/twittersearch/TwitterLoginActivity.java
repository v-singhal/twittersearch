package com.vbstudio.twittersearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vbstudio.twittersearch.fragment.BaseFragment;
import com.vbstudio.twittersearch.fragment.TwitterLoginFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.vbstudio.twittersearch.utils.StringUtils.isValidString;

public class TwitterLoginActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private int containerId = R.id.webViewContainer;
    public static String EXTRA_URL = "extra_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        configureToolbar();
        Intent intentForPayment = getIntent();

        if (intentForPayment != null) {
            /****Passage of intent content****/
            if (intentForPayment.getExtras() != null) {
                String url = intentForPayment.getExtras().getString(TwitterLoginActivity.EXTRA_URL);
                if(isValidString(url)) {
                    openTwitterLoginFrament(url);
                } else {
                    Toast.makeText(this, "Login url is malformed", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String title = "";
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(containerId);
        if (fragment != null) {
            title = fragment.getTitle();
        }
        setTitle(title);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(containerId);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView titleView = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.title);
        titleView.setText(title);
    }

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        getSupportActionBar().setCustomView(R.layout.toolbar_view);
        getSupportActionBar().getCustomView().findViewById(R.id.btnLogout).setVisibility(View.GONE);
    }

    private void openTwitterLoginFrament(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        TwitterLoginFragment twitterLoginFragment = (TwitterLoginFragment) TwitterLoginFragment.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(containerId, twitterLoginFragment).commit();
    }
}