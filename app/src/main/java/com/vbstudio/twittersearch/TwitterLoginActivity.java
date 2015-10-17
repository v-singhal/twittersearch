package com.vbstudio.twittersearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.vbstudio.twittersearch.fragment.TwitterWebLoginFragment;

import static com.vbstudio.twittersearch.utils.StringUtils.isValidString;

public class TwitterLoginActivity extends BaseActivity {

    private static Intent launchIntent;
    public static String EXTRA_URL = "extra_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twitter_login_activity);

        configureToolbar();
        Intent intentForLogin = getIntent();

        verifyIntentForLoginUrl(intentForLogin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
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

    private void configureToolbar() {
        super.configureToolbar((Toolbar) findViewById(R.id.appToolbar));
    }

    private void verifyIntentForLoginUrl(Intent intentForLogin) {
        if (intentForLogin != null) {
            /****Passage of intent content****/
            if (intentForLogin.getExtras() != null) {
                String url = intentForLogin.getExtras().getString(TwitterLoginActivity.EXTRA_URL);
                if (isValidString(url)) {
                    launchIntent = intentForLogin;
                    openTwitterLoginFragment(url);
                } else {
                    Toast.makeText(this, "Login url is malformed", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }
        }
    }

    private void openTwitterLoginFragment(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        TwitterWebLoginFragment twitterLoginFragment = (TwitterWebLoginFragment) TwitterWebLoginFragment.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(BaseActivity.TWITTER_LOGIN_ACTIVITY_CONTAINER_ID, twitterLoginFragment).commit();
    }

    public static void relaunchActivity(Context context) {
        context.startActivity(launchIntent);
    }

    public void reloadLoginFragment(Context context) {
        verifyIntentForLoginUrl(launchIntent);
    }
}