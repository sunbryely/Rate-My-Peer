package com.gevdev.stalky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
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


public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public static AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "Login";
    private Tracker mTracker;
    private Toolbar toolbar;
    public static String userName;
    public static String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MemberServiceCenter(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        accessToken = AccessToken.getCurrentAccessToken();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        callbackManager = CallbackManager.Factory.create();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Launched")
                .build());


        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // App code
//                        info.setText(
//                                "User ID: " + loginResult.getAccessToken().getUserId()
//                                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()
//                        );

                        //onLogin();

                        myID = loginResult.getAccessToken().getUserId();

                        userName = "https://graph.facebook.com/"+myID+"?fields=first_name"
                                + "https://graph.facebook.com/"+myID+"?fields=last_name";

                        accessToken = loginResult.getAccessToken();

                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Login")
                                .setAction("Successfull Facebook Login")
                                .build());


                        String URL = String.format("http://54.149.222.140/login");
                        //JSONObject objectToPost = new JSONObject();
                        //objectToPost.put(key, value);
                        JSONObject params = new JSONObject();
                        try {
                            params.put("userId", loginResult.getAccessToken().getUserId());
                            params.put("userToken", loginResult.getAccessToken().getToken());
                        } catch( org.json.JSONException e ) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonRequest = new JsonObjectRequest
                                (Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        Log.i("json", jsonObject.toString());
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        volleyError.printStackTrace();
                                        System.out.println("hererererer");
                                    }
                                });

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
    public void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
        String name = "Facebook Login Screen";

        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Resumed on page: " + name)
                .build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);

        String name = "Facebook Login Screen";

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Paused on: " + name)
                .build());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
    }



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


    private void updateWithToken(AccessToken currentAccessToken) {
        myID = currentAccessToken.getUserId();

        userName = "https://graph.facebook.com/"+myID+"?fields=first_name"
                + "https://graph.facebook.com/"+myID+"?fields=last_name";

        if(currentAccessToken != null) onLogin();
    }


}
