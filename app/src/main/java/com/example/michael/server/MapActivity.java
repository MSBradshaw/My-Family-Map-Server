package com.example.michael.server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.Model.Event;

/**
 * Created by Michael on 4/3/2017.
 */

public class MapActivity extends AppCompatActivity {
    private static MainActivity mainActivity;
    private static Event event = null;

    public static void setEvent(Event event) {
        MapActivity.event = event;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MapActivity.mainActivity = mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        fragment = new MapFragment();
        MapFragment mapFragment = new MapFragment();
        double lat = Double.parseDouble(event.getLatitude());
        double lng = Double.parseDouble(event.getLongitude());
        mapFragment.setLat(lat);
        mapFragment.setLng(lng);
        mapFragment.setMainActivity(mainActivity); //gives LoginFragment access to this class
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();

    }
}
