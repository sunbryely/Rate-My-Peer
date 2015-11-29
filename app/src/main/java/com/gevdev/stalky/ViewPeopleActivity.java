package com.gevdev.stalky;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Service.MemberServiceCenter;

public class ViewPeopleActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView name;
    private TextView friendliness;
    private TextView skills;
    private TextView teamwork;
    private TextView funfactor;
    private RatingBar friendStar;
    private RatingBar skillStar;
    private RatingBar teamStar;
    private RatingBar funStar;
    private ListView list;
    private List<String> commentsList = new ArrayList<>();
    private Button rateBtn;
    private String searchedId;
    private static final String TAG = "ViewPeopleFragment";
    private Tracker mTracker;
    Toolbar toolbar;

    private String[] SUGGESTIONS;
    private SimpleCursorAdapter mAdapter;
    ArrayList<String[]> nameList;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        nameList = new ArrayList<>();


        String[] from = new String[]{"names"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        setContentView(R.layout.user_profile_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //initView
        profileImage = (ImageView) findViewById(R.id.user_profile_image);
        name = (TextView) findViewById(R.id.profile_name);
        friendliness = (TextView) findViewById(R.id.friendliness_score);
        skills = (TextView) findViewById(R.id.skills_score);
        teamwork = (TextView) findViewById(R.id.teamwork_score);
        funfactor = (TextView) findViewById(R.id.funfactor_score);
        friendStar = (RatingBar) findViewById(R.id.friendliness_rating);
        skillStar = (RatingBar) findViewById(R.id.skills_rating);
        teamStar = (RatingBar) findViewById(R.id.teamwork_rating);
        funStar = (RatingBar) findViewById(R.id.funfactor_rating);
        list = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = null;
        list.setAdapter(arrayAdapter);

        rateBtn = (Button) findViewById(R.id.rate_btn);

        //TODO
//        rateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ViewPeopleActivity.this, RateActivity.class));
//            }
//        });


        //get searchedId
        Intent intent = getIntent();
        String profileName = intent.getStringExtra("profileName");
        name.setText(profileName);
        String searchedId = intent.getStringExtra("searchedId");

        String userID = "100006683413828";
        String imageURL = "https://graph.facebook.com/" + userID + "/picture?type=large";
        Picasso.with(this).load(imageURL).into(profileImage);

        //String URL= String.format("54.149.222.140/users/%s", searched_id);
        String URL = String.format("http://54.149.222.140/users/%s", "100006683413828");
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "jsonObject = " + jsonObject.toString());
                        updateUI(jsonObject);
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

    }

    protected void updateUI(JSONObject obj) {
        try {
            JSONObject ratingObj = obj.getJSONObject("ratings");
            String friendScore = ratingObj.getString("friendliness");
            friendliness.setText(friendScore);
            friendStar.setRating(Float.parseFloat(friendScore));

            String skillScore = ratingObj.getString("skill");
            skills.setText(skillScore);
            skillStar.setRating(Float.parseFloat(skillScore));

            String teamScore = ratingObj.getString("teamwork");
            teamwork.setText(teamScore);
            teamStar.setRating(Float.parseFloat(teamScore));

            String funScore = ratingObj.getString("funfactor");
            funfactor.setText(funScore);
            funStar.setRating(Float.parseFloat(funScore));

            JSONArray commentsArray = obj.getJSONArray("comments");
            for (int i = 0; i < commentsArray.length(); i++) {
                commentsList.add(commentsArray.getJSONObject(i).getString("comment"));
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    ViewPeopleActivity.this,
                    android.R.layout.simple_list_item_1,
                    commentsList);
            list.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSuggestionsAdapter(mAdapter);


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                String name = getSuggestion(position);
                searchView.setQuery(name, true); // submit query now

                updateProfile(name);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;

                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText.length() >= 3)
                            getNames(newText);

                        return false;
                    }
                }
        );


        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = "View Profile Page";

        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Resumed on: " + name)
                .build());
    }

    @Override
    protected void onPause() {
        super.onPause();

        String name = "View Profile Page";

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Paused on: " + name)
                .build());
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "names"});
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }

    private void getNames(final String name) {
        String URL = String.format("http://54.149.222.140/user/%s", name);


        JsonArrayRequest jsonRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                addNames(response);
                populateAdapter(name);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MemberServiceCenter.requestQueue.add(jsonRequest);


    }

    public void addNames(JSONArray jArray) {
        String[] suggestions;

        suggestions = new String[jArray.length()];

        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);

                String name = obj.getString("name");
                String userID = obj.getString("facebook_id");
                String[] strArr = {name, userID};
                nameList.add(strArr);
                suggestions[i] = name;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SUGGESTIONS = suggestions;
    }

    public void updateProfile(String name) {

        String userID = "";

        for(int i = 0; i<nameList.size(); i++) {
            System.err.println(name);
            if(name.equals(nameList.get(i)[0])) {
                System.err.println(nameList.get(i)[0] + " : " + nameList.get(i)[1]);
                userID = nameList.get(i)[1];
                break;
            }
        }

        String imageURL = "https://graph.facebook.com/" + userID + "/picture?type=large";
        Picasso.with(this).load(imageURL).into(profileImage);

        String URL = String.format("http://54.149.222.140/users/%s", userID);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "jsonObject = " + jsonObject.toString());
                        updateUI(jsonObject);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }

                });
        MemberServiceCenter.requestQueue.add(jsonRequest);
    }

    private void onLogin() {
        Intent intent = new Intent(this, ViewPeopleActivity.class);
        startActivity(intent);
    }

    private String getSuggestion(int position) {
        Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
        String suggest1 = cursor.getString(cursor.getColumnIndex("names"));
        return suggest1;
    }
}
