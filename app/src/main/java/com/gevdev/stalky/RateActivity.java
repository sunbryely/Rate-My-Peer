package com.gevdev.stalky;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xml.sax.helpers.DefaultHandler;

import Service.MemberServiceCenter;

/**
 * Created by zhuchen on 10/27/15.
 */
public class RateActivity extends Activity{
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_activity_layout);

        addListenerOnRatingBar();
        addSubmitListener();
    }

    //Listener for all the rating bars
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
            public void onRatingChanged(RatingBar friendRatingBar, float rating,  boolean fromUser) {
                friendValue.setText(String.valueOf(rating));
                friendStars = friendRatingBar.getRating();
            }
        });

        //set listener for skills ratings bar
        skillsRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar skillsRatingBar, float rating,  boolean fromUser) {
                skillsValue.setText(String.valueOf(rating));
                skillsStars = skillsRatingBar.getRating();
            }
        });

        //set listener for team ratings bar
        teamRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar teamRatingBar, float rating,  boolean fromUser) {
                teamValue.setText(String.valueOf(rating));
                teamStars = teamRatingBar.getRating();
            }
        });

        //set listener for friend ratings bar
        funRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar funRatingBar, float rating,  boolean fromUser) {
                funValue.setText(String.valueOf(rating));
                funStars = funRatingBar.getRating();
            }
        });

    }

    //Listen for click on submit button
    public void addSubmitListener(){

        // get comment section
        comments = (EditText) findViewById(R.id.editText);
        submitBtn = (Button) findViewById(R.id.submit_btn);


        //submit the rating
    }

    //Initialize Side Nav
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        //pass your items here
                )
                .build();
        return true;
    }

}
