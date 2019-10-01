package com.uni.localvillage.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.localvillage.R;
import com.uni.localvillage.fragments.LoginFragment;

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
    }
}
