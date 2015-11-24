package com.gevdev.stalky;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.materialdrawer.DrawerBuilder;

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

    private Tracker mTracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_activity_layout);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

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
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    //Listen for click on submit button
    public void addSubmitListener(){

        // get comment section
        comments = (EditText) findViewById(R.id.editText);
        submitBtn = (Button) findViewById(R.id.submit_btn);


        //submit the rating
    }

    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Paused")
                .build());
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
