package com.gevdev.stalky;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

/**
 * Created by zhuchen on 10/27/15.
 */
public class ViewPeopleActivity  extends Activity {
    private TextView friendliness;
    private TextView skills;
    private TextView teamwork;
    private TextView funfactor;
    private Button rate;

    private LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);
    }
    
}
