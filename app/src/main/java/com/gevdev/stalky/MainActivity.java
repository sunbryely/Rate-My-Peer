package com.gevdev.stalky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Service.MemberServiceCenter;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.gevdev.Stalky.MESSAGE";

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final String TAG = "Login";
    //Used for analytics
    private Tracker mTracker;

    public static AccessToken accessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        new MemberServiceCenter(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);



        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // App code
                        /*info.setText(
                                "User ID: " + loginResult.getAccessToken().getUserId()
                                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()
                        );*/

                        //onLogin();
                        accessToken = loginResult.getAccessToken();

                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Login")
                                .setAction("Successfull Facebook Login")
                                .build());



                        String URL = String.format("http://54.149.222.140/login");
                        //JSONObject objectToPost = new JSONObject();
                        //objectToPost.put(key, value);
                        JsonObjectRequest jsonRequest = new JsonObjectRequest
                                (Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        Log.i("json", jsonObject.toString());
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        volleyError.printStackTrace();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userId", loginResult.getAccessToken().getUserId());
                                params.put("userToken", loginResult.getAccessToken().getToken());
                                return params;
                            }
                        };

                        MemberServiceCenter.requestQueue.add(jsonRequest);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        info.setText("Login attempt was cancelled");

                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Login")
                                .setAction("Facebook Login Cancelled by User")
                                .build());
                    }

                    @Override
                    public void onError(FacebookException e) {
                        // App code
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Login")
                                .setAction("Error with Facebook Login")
                                .build());
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
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Paused")
                .build());
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
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("SideMenu")
                                .setAction("View People Clicked from SideMenu")
                                .build());
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

    /**
     * Return the title of the currently displayed image.
     * @return title of image
     */


}

