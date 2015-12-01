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

    private boolean commentSuccess = false;
    private boolean ratingSuccess = false;


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

        initView();
        updateView();

        addListenerOnRatingBar();
        try {
            addSubmitListener();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        friendRatingBar = (RatingBar) findViewById(R.id.friendliness_rating);
        skillsRatingBar = (RatingBar) findViewById(R.id.skills_rating);
        teamRatingBar = (RatingBar) findViewById(R.id.teamwork_rating);
        funRatingBar = (RatingBar) findViewById(R.id.funfactor_rating);

        friendValue = (TextView) findViewById(R.id.friendliness_score);
        skillsValue = (TextView) findViewById(R.id.skills_score);
        teamValue = (TextView) findViewById(R.id.teamwork_score);
        funValue = (TextView) findViewById(R.id.funfactor_score);

    }

    private void updateView() {

        String user_id_from = MainActivity.myID;
        String user_id_to = ViewPeopleActivity.viewID;

        if (MainActivity.myID != null) Log.e("idd", MainActivity.myID);
        else Log.e("idd", "its null");
        if (ViewPeopleActivity.viewID != null) Log.e("idd", ViewPeopleActivity.viewID);
        else Log.e("idd", "its null");

        // update comment
        String commentURL = String.format("http://54.149.222.140/comment?user_id_from=%s&user_id_to=%s", user_id_from, user_id_to);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, commentURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("115: RESPONSE", "SUCCESS");
                        Log.e("comment", "jsonObject = " + jsonObject.toString());
                        try {
                            String comment = jsonObject.getString("comment");
                            comments.setText(comment);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("127: RESPONSE", "FAILURE");
                        volleyError.printStackTrace();
                    }
                });
        MemberServiceCenter.requestQueue.add(jsonRequest);

        // update rating
        String ratingURL = String.format("http://54.149.222.140/rate?user_id_from=%s&user_id_to=%s", user_id_from, user_id_to);
        JsonObjectRequest jsonRequest2 = new JsonObjectRequest
                (Request.Method.GET, ratingURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("139: RESPONSE", "SUCCESS");
                        Log.e("rating", "jsonObject = " + jsonObject.toString());

                        try {
                            String friendScore = jsonObject.getString("rating_friendliness");
                            friendValue.setText(friendScore);
                            friendRatingBar.setRating(Float.parseFloat(friendScore));

                            String skillScore = jsonObject.getString("rating_skill");
                            skillsValue.setText(skillScore);
                            skillsRatingBar.setRating(Float.parseFloat(skillScore));

                            String teamScore = jsonObject.getString("rating_teamwork");
                            teamValue.setText(teamScore);
                            teamRatingBar.setRating(Float.parseFloat(teamScore));

                            String funScore = jsonObject.getString("rating_funfactor");
                            funValue.setText(funScore);
                            funRatingBar.setRating(Float.parseFloat(funScore));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("166: RESPONSE", "FAILURE");
                        volleyError.printStackTrace();
                    }
                });
        MemberServiceCenter.requestQueue.add(jsonRequest2);
    }

    public void addListenerOnRatingBar(){

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


        ratings.put("user_id_from", MainActivity.myID);
        ratings.put("user_id_to", ViewPeopleActivity.viewID);

        comment.put("user_id_from", MainActivity.myID);
        comment.put("user_id_to", ViewPeopleActivity.viewID);

        //submit the rating
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    comment.put("comment", comments.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // add comment
                String commentURL = String.format("http://54.149.222.140/comments");
                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, commentURL, comment, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("253: RESPONSE", "SUCCESS");
                                commentSuccess = true;
                                if (ratingSuccess) {
                                    finish();
                                    Intent i = new Intent(RateActivity.this, ViewPeopleActivity.class);
                                    startActivity(i);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("260: RESPONSE", "FAILURE");
                                volleyError.printStackTrace();
                            }
                        });
                MemberServiceCenter.requestQueue.add(jsonRequest);


                try {
                    ratings.put("friendliness", friendStars);
                    ratings.put("skill", skillsStars);
                    ratings.put("teamwork", teamStars);
                    ratings.put("funfactor", funStars);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // add rating
                String ratingURL = String.format("http://54.149.222.140/rate");
                JsonObjectRequest jsonRequest2 = new JsonObjectRequest
                        (Request.Method.POST, ratingURL, ratings, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("282: RESPONSE", "SUCCESS");
                                ratingSuccess = true;
                                if (commentSuccess) {
                                    finish();
                                    Intent i = new Intent(RateActivity.this, ViewPeopleActivity.class);
                                    RateActivity.this.startActivity(i);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("289: RESPONSE", "FAILURE");
                                volleyError.printStackTrace();
                            }
                        });
                MemberServiceCenter.requestQueue.add(jsonRequest2);

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
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("My Profile ");

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withRootView(R.id.drawer_layout)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        //pass your items here
                        item1, item2
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
