package com.example.michael.server;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new LoginFragment();
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setMainActivity(this); //gives LoginFragment access to this class
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    public void startMapFrag(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        fragment = new MapFragment();
        MapFragment mapFragment = new MapFragment();
        mapFragment.setLat(36.1667); //nashville
        mapFragment.setLng(-85.2167);
        mapFragment.setMainActivity(this);
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
