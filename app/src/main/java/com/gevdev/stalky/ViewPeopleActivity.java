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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Service.MemberServiceCenter;
import adapter.FullyLinearLayoutManager;
import adapter.RecyclerViewAdapter;
import bean.Comment;

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
    private List<Comment> commentsList = new ArrayList<>();
    private Button rateBtn;
    private String searchedId;
    private static final String TAG = "ViewPeopleFragment";
    private Tracker mTracker;
    Toolbar toolbar;

    public static String viewID = ""; //ID of the person who's profile we are looking at

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;


    private String[] SUGGESTIONS;
    private SimpleCursorAdapter mAdapter;
    ArrayList<String[]> nameList;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        nameList = new ArrayList<>();
        String[] myStrArr = {MainActivity.userName, MainActivity.myID};
        nameList.add(myStrArr);


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
        getSupportActionBar().setTitle("My Profile");


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

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(this));

        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);


        rateBtn = (Button) findViewById(R.id.rate_btn);

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ViewPeopleActivity.this, RateActivity.class), 1);
            }
        });


        //get searchedId
        Intent intent = getIntent();
        String profileName = intent.getStringExtra("profileName");
        name.setText(profileName);
        String searchedId = intent.getStringExtra("searchedId");

        String userID = "";
        if(viewID.equals("")) userID = MainActivity.myID;
        else                  userID = viewID;
        String imageURL = "https://graph.facebook.com/" + userID + "/picture?type=large";
        System.err.println("MY ID IS: " + MainActivity.myID);
        System.err.println("MY Name is: " + MainActivity.userName);
        Picasso.with(this).load(imageURL).into(profileImage);

        //String URL= String.format("54.149.222.140/users/%s", searched_id);
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

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MemberServiceCenter.requestQueue.add(jsonRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                finish();
        }
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
            commentsList.clear();
            for (int i = 0; i < commentsArray.length(); i++) {
                Comment cur = new Comment();
                cur.comment = commentsArray.getJSONObject(i).getString("comment");
                if (cur.comment.equals("")) continue;
                cur.user_id_from = commentsArray.getJSONObject(i).getString("user_id_from");
                cur.updated_at = commentsArray.getJSONObject(i).getString("updated_at");
                commentsList.add(cur);

            }
            System.out.println("size " + commentsList.size());
            recyclerViewAdapter.setItems(commentsList);

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

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("My Profile");

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

                            updateProfile(MainActivity.userName);
                            mTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("SideMenu")
                                    .setAction("View My Profile Clicked from SideMenu")
                                    .build());


                        return true;
                    }
                })
                .build();


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

        System.err.println("Name is: " + name);
        Log.i("NAME", name);

        getSupportActionBar().setTitle(name + "'s Profile");

        String userID = "";

        for(int i = 0; i<nameList.size(); i++) {
            if(name.equals(nameList.get(i)[0])) {
                userID = nameList.get(i)[1];
                viewID = userID;
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
