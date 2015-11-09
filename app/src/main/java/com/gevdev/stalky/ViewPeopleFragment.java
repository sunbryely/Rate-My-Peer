package com.gevdev.stalky;

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

public class ViewPeopleFragment extends Fragment {
    private TextView friendliness;
    private TextView skills;
    private TextView teamwork;
    private TextView funfactor;
    private Button rateBtn;
    private String searchedId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_layout,null);

        //initView
        friendliness = (TextView)findViewById(R.id.friendliness_score);
        skills = (TextView) findViewById(R.id.skills_score);
        teamwork = (TextView) findViewById(R.id.teamwork_score);
        funfactor = (TextView) findViewById(R.id.funfactor_score);
        rateBtn = (Button) findViewById(R.id.rate_btn);

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RateActivity.class));
            }
        });
        Intent intent = getIntent();
        String searchedId = intent.getStringExtra("searchedId");
        MemberServiceCenter.getSearchedUserInfo(searchedId);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
