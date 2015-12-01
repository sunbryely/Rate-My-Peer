package com.gevdev.stalky;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.Menu;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Service.MemberServiceCenter;
import bean.Comment;

public class RankingsActivity extends AppCompatActivity {

    private Tracker mTracker;

    public class userRanking {
        String username;
        String facebook_id;
        double rating;
    }

    rankingsAdapter rankingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Google Analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setContentView(R.layout.rankings_layout);

        // Initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rankingsAdapter = new rankingsAdapter();

    }

    public class rankingsAdapter extends BaseAdapter {
        List<userRanking> rankList = getDataForListView();

        @Override
        public int getCount() {
            return rankList.size();
        }

        @Override
        public userRanking getItem(int item) {
            return rankList.get(item);
        }

        @Override
        public long getItemId(int item) {
            return item;
        }

        @Override
        public View getView(int arg, View view, ViewGroup group) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) RankingsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.rankings_row, group, false);
            }

            // Connect with layout file
            TextView username = (TextView) view.findViewById(R.id.name);
            TextView rating = (TextView) view.findViewById(R.id.rating);
            ImageView profilePic = (ImageView) view.findViewById(R.id.user_profile);

            String userID = "100006683413828";
            String imageURL = "https://graph.facebook.com/" + userID + "/picture?type=large";
            Picasso.with(this).load(imageURL).into(profilePic);

            userRanking ranking = rankList.get(arg);

            username.setText(ranking.username);
            rating.setText(String.valueOf(ranking.rating));

            return view;
        }

        public userRanking getUserRanking(int position) {

            return rankList.get(position);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.rankings_layout, menu);
//        return true;
//    }

    /**
     * Handles logic for returning the data for users.
     * @return
     */
    public List<userRanking> getDataForListView() {
        final List<userRanking> rankList = new ArrayList<userRanking>();

        String URL = String.format("http://54.149.222.140/top");
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.e(TAG, "jsonObject = " + jsonArray.toString());
                        updateUI(jsonArray, rankList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }

                });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MemberServiceCenter.requestQueue.add(jsonRequest);


        return rankList;
    }

    protected void updateUI(JSONArray array, List list) {
        try {

            for (int i = 0; i < array.length(); i++) {
                userRanking ranking = new userRanking();
                ranking.username = array.getJSONObject(i).getString("name");
                ranking.rating = array.getJSONObject(i).getDouble("avg");
                ranking.facebook_id = array.getJSONObject(i).getString("facebook_id");
                list.add(i, array.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
