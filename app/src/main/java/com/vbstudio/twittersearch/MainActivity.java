package com.vbstudio.twittersearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vbstudio.twittersearch.dialog.LogoutConfirmationDialog;
import com.vbstudio.twittersearch.fragment.BaseFragment;
import com.vbstudio.twittersearch.fragment.LoginFragment;
import com.vbstudio.twittersearch.fragment.TwitterSearchFragment;
import com.vbstudio.twittersearch.preferences.HaptikAssignmentPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();
        openAptFragment();
    }

    private void openAptFragment() {
        BaseFragment fragment = new BaseFragment();

        if(HaptikAssignmentPreferences.getUserLoginState(this)) {
            fragment = TwitterSearchFragment.newInstance();
        }
        else {
            fragment = LoginFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String title = "";
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            title = fragment.getTitle();
        }
        setTitle(title);
        setLogoutButton(fragment);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView titleView = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.title);
        titleView.setText(title);
    }

    private void setLogoutButton(BaseFragment fragment) {
        Boolean isLogoutEnabled = fragment.isLogoutEnabled();

        if(isLogoutEnabled) {
            getSupportActionBar().getCustomView().findViewById(R.id.btnLogout).setVisibility(View.VISIBLE);
            getSupportActionBar().getCustomView().findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    openLogoutConfirmationDialog();
                }
            });
        } else {
            getSupportActionBar().getCustomView().findViewById(R.id.btnLogout).setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        return;
    }

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        getSupportActionBar().setCustomView(R.layout.toolbar_view);
    }

    private void openLogoutConfirmationDialog() {
        LogoutConfirmationDialog logoutConfirmationDialog = new LogoutConfirmationDialog(this);
        BaseFragment.openAniamtedDialog(logoutConfirmationDialog);
    }

    public void performActionOnSignupAndLogout() {
        HaptikAssignmentPreferences.clearAll(this);
        getSupportActionBar().getCustomView().findViewById(R.id.btnLogout).setVisibility(View.GONE);
        BaseFragment.replaceStack(this, LoginFragment.newInstance());
    }
}
