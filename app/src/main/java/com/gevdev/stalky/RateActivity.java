package com.gevdev.stalky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

    private Tracker mTracker;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        addListenerOnRatingBar();
        addSubmitListener();

        setContentView(R.layout.rate_activity_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public void addSubmitListener(){

        // get comment section
        comments = (EditText) findViewById(R.id.editText);
        submitBtn = (Button) findViewById(R.id.submit_btn);

        //submit the rating
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
