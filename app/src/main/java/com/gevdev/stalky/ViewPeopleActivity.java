package com.gevdev.stalky;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.logging.Logger;

import Service.MemberServiceCenter;
import bean.User;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);
        //insert and active navBar
        /*NavBarFragment navBar = new NavBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navBar, navBar);
        transaction.commit();*/

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
        Intent intent = getIntent();
        String searchedId = intent.getStringExtra("searchedId");
        //User user = MemberServiceCenter.getInstance(ViewPeopleActivity.this).getSearchedUserInfo(searchedId);

        //init Data
        /*friendliness.setText(user.getFriendiScore());
        skills.setText(user.getSkillsScore());
        teamwork.setText(user.getTeamworkScore());
        funfactor.setText(user.getFunScore());*/


    }

}
