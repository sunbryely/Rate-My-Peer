package com.gevdev.stalky;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import adapter.ViewPagerAdapter;

public class RankingsActivity extends AppCompatActivity {

    private Tracker mTracker;
    private static final String TAG = "Rankings";
    private Toolbar toolbar;

    public class userRanking {
        String username;
        String facebook_id;
        double rating;
    }

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Google Analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setContentView(R.layout.rankings_layout);
        setTitle("Top Five");

        // Initialize toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViewAdapter();
    }

    private void setViewAdapter() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();

        String name = "Ranking Screen";

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

        String name = "Ranking Screen";

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Paused on: " + name)
                .build());

    }

}
