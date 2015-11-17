package com.gevdev.stalky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.helpers.DefaultHandler;

import Service.MemberServiceCenter;

/**
 * view people's rating profile
 * @author Sherry
 */

public class ViewPeopleActivity extends Activity {
    private TextView friendliness;
    private TextView skills;
    private TextView teamwork;
    private TextView funfactor;
    private Button rateBtn;
    private String searchedId;
    private static final String TAG = "ViewPeopleFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);

        //initView
        friendliness = (TextView)findViewById(R.id.friendliness_score);
        skills = (TextView) findViewById(R.id.skills_score);
        teamwork = (TextView) findViewById(R.id.teamwork_score);
        funfactor = (TextView) findViewById(R.id.funfactor_score);
        rateBtn = (Button) findViewById(R.id.rate_btn);

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewPeopleActivity.this, RateActivity.class));
            }
        });

        //get searchedId
        /*Intent intent = getIntent();
        String searchedId = intent.getStringExtra("searchedId");

        String URL= String.format("54.149.222.140/users/%s", searched_id);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "jsonObject = " + jsonObject.toString());


                        try {
                            JSONArray jArray = jsonObject.getJSONArray("searched_id");
                            Log.e(TAG, "contacts = " + jArray.toString());
                            updateUI(jArray);

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

        MemberServiceCenter.requestQueue.add(jsonRequest);*/
    }

    /*protected void updateUI(JSONArray jArray) {
        try {

            JSONObject obj0 = jArray.getJSONObject(0);
            friendliness.setText(obj0.getString("friendliness"));
            JSONObject obj1 = jArray.getJSONObject(1);
            skills.setText(obj1.getString("skills"));
            JSONObject obj2 = jArray.getJSONObject(2);
            teamwork.setText(obj2.getString("teamwork"));
            JSONObject obj3 = jArray.getJSONObject(3);
            funfactor.setText(obj3.getString("funfactor"));

        }catch (JSONException e) {

        }

    }*/
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
