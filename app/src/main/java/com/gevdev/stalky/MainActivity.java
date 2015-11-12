package com.gevdev.stalky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import Service.MemberServiceCenter;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.gevdev.Stalky.MESSAGE";

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        new MemberServiceCenter(this);


        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        /*info.setText(
                                "User ID: " + loginResult.getAccessToken().getUserId()
                                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()
                        );*/

                        //onLogin();


                    }

                    @Override
                    public void onCancel() {
                        // App code
                        info.setText("Login attempt was cancelled");

                    }

                    @Override
                    public void onError(FacebookException e) {
                        // App code
                        info.setText("Login Attempt failed due to an error " + e.toString() +
                                ". WHY WON'T ANYONE HELP ME");
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("View People");

        new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        //pass your items here
                        item1
                        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        onLogin();
                        return true;
                    }
                })
                .build();
        return true;
    }

/*
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
    }*/


    /*
        ========================= GEV ================================
        This method will execute some actions as the MainActivity loads

        If a user is logged in, create a new Intent and switch to the the user search activity
        which will be where users can search for people to rate.

        If a user is not logged in, just let the user login, and the callback from the FB
        login function will be used to move to the search activity.
     */
    private void onLogin() {
        Intent intent = new Intent(this, ViewPeopleActivity.class);
        startActivity(intent);
    }

    /*
        ========================== GEV =======================
        This method will return true if facebook user is already logged in via facebook,
        false otherwise
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


}

