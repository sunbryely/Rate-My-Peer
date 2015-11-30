package com.gevdev.stalky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import Service.MemberServiceCenter;

public class RateActivity extends AppCompatActivity {

    private Button submitBtn;
    private EditText comments;
    private RatingBar friendRatingBar;
    private RatingBar skillsRatingBar;
    private RatingBar teamRatingBar;
    private RatingBar funRatingBar;
    private TextView friendValue;
    private TextView skillsValue;
    private TextView teamValue;
    private TextView funValue;

    //rating to be passed to backend
    private float friendStars;
    private float skillsStars;
    private float teamStars;
    private float funStars;

    JSONObject ratings = new JSONObject();
    JSONObject comment = new JSONObject();


    private Tracker mTracker;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setContentView(R.layout.rate_activity_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addListenerOnRatingBar();
        try {
            addSubmitListener();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addListenerOnRatingBar(){
        friendRatingBar = (RatingBar) findViewById(R.id.friendliness_rating);
        skillsRatingBar = (RatingBar) findViewById(R.id.skills_rating);
        teamRatingBar = (RatingBar) findViewById(R.id.teamwork_rating);
        funRatingBar = (RatingBar) findViewById(R.id.funfactor_rating);

        friendValue = (TextView) findViewById(R.id.friendliness_score);
        skillsValue = (TextView) findViewById(R.id.skills_score);
        teamValue = (TextView) findViewById(R.id.teamwork_score);
        funValue = (TextView) findViewById(R.id.funfactor_score);


        //set listener for friend ratings bar
        friendRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar friendRatingBar, float rating, boolean fromUser) {
                friendValue.setText(String.valueOf(rating));
                friendStars = friendRatingBar.getRating();
            }
        });

        //set listener for skills ratings bar
        skillsRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar skillsRatingBar, float rating, boolean fromUser) {
                skillsValue.setText(String.valueOf(rating));
                skillsStars = skillsRatingBar.getRating();
            }
        });

        //set listener for team ratings bar
        teamRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar teamRatingBar, float rating, boolean fromUser) {
                teamValue.setText(String.valueOf(rating));
                teamStars = teamRatingBar.getRating();
            }
        });

        //set listener for friend ratings bar
        funRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar funRatingBar, float rating, boolean fromUser) {
                funValue.setText(String.valueOf(rating));
                funStars = funRatingBar.getRating();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = "Rate People Screen";

        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Resumed on: " + name)
                .build());
    }

    //Listen for click on submit button
    public void addSubmitListener() throws JSONException {

        // get comment section
        comments = (EditText) findViewById(R.id.editText);
        submitBtn = (Button) findViewById(R.id.submit_btn);


        ratings.put("friendliness", friendStars);
        ratings.put("skill", skillsStars);
        ratings.put("teamwork", teamStars);
        ratings.put("funcactor", funStars);

        comment.put("user_id_from", "10153269447328549");
        comment.put("user_id_to", "100006683413828");
        comment.put("comment", comments.getText().toString());

        final JSONObject jsonObject = new JSONObject();
        final JSONObject rating = new JSONObject();
        jsonObject.put("friendliness", friendStars);
        jsonObject.put("skill", skillsStars);
        jsonObject.put("teamwork", teamStars);
        jsonObject.put("funcactor", funStars);
        rating.put("facebook_id", "10153269447328549");
        rating.put("ratings", jsonObject);


        //submit the rating
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = String.format("http://54.149.222.140/comments");

                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, URL, comment, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("RESPONSE", "SUCCESS");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("RESPONSE", "FAILURE");
                                volleyError.printStackTrace();
                            }
                        });
                MemberServiceCenter.requestQueue.add(jsonRequest);
            }
        });
    }

    protected void onPause() {
        super.onPause();

        String name = "Rate People Screen";

        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Paused on: " + name)
                .build());
    }

    //Initialize Side Nav
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("View People");

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withRootView(R.id.drawer_layout)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
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

    private void onLogin() {
        Intent intent = new Intent(this, ViewPeopleActivity.class);
        startActivity(intent);
    }

}
