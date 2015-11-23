package com.gevdev.stalky;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Service.MemberServiceCenter;


/**
 * view people's rating profile
 * @author Sherry
 */

public class ViewPeopleActivity extends Activity {
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
    private List<String> commentsList = new ArrayList<String>();
    private Button rateBtn;
    private String searchedId;
    private static final String TAG = "ViewPeopleFragment";
    private Tracker mTracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //initView
        profileImage = (ImageView) findViewById(R.id.user_profile_image);
        name = (TextView) findViewById(R.id.profile_name);
        friendliness = (TextView)findViewById(R.id.friendliness_score);
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

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewPeopleActivity.this, RateActivity.class));
            }
        });

        //get searchedId
        Intent intent = getIntent();
        String profileName = intent.getStringExtra("profileName");
        name.setText(profileName);
        String searchedId = intent.getStringExtra("searchedId");

        //get picture url
        final String[] url = new String[1];
//        String path = "/" + search_id + "/picture";
        String path = "/100006683413828/picture";
        GraphRequest request = GraphRequest.newGraphPathRequest(
                                MainActivity.accessToken,
                                path,
                                new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject json =  response.getJSONObject();
                try {
                    url[0] = json.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("type", "large");
        request.setParameters(parameters);
        request.executeAsync();

        //get picture task
        Drawable d = LoadImageFromWebOperations(url[0]);
        profileImage.setImageDrawable(d);


        //String URL= String.format("54.149.222.140/users/%s", searched_id);
        String URL= String.format("http://54.149.222.140/users/%s", "100006683413828");
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "jsonObject = " + jsonObject.toString());


                        try {
                            JSONObject obj = jsonObject.getJSONObject("searched_id");
                            Log.e(TAG, "contacts = " + obj.toString());
                            updateUI(obj);

                        } catch (JSONException e) {
                            Log.e(TAG, "error");
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }

                });

        MemberServiceCenter.requestQueue.add(jsonRequest);
    }

    protected void updateUI(JSONObject obj) {
        try {
            String friendScore = obj.getString("friendliness");
            friendliness.setText(friendScore);
            friendStar.setRating(Float.parseFloat("friendScore"));

            String skillScore = obj.getString("skills");
            skills.setText(skillScore);
            skillStar.setRating(Float.parseFloat("skillScore"));

            String teamScore = obj.getString("teamwork");
            teamwork.setText(teamScore);
            skillStar.setRating(Float.parseFloat("teamScore"));

            String funScore = obj.getString("funfactor");
            funfactor.setText(funScore);
            funStar.setRating(Float.parseFloat("funScore"));

            JSONArray commentsArray = obj.getJSONArray("comments");
            for (int i = 0; i < commentsArray.length(); i++) {
                commentsList.add(commentsArray.getJSONObject(i).getString("comment"));
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                commentsList);
            list.setAdapter(arrayAdapter);
        }catch (JSONException e) {

        }

    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "profilePicture");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

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


    protected void onResume() {
        super.onResume();
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Paused")
                .build());
    }

}
