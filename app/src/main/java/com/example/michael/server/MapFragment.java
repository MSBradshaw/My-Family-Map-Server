package com.example.michael.server;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.IconButton;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Event;
import com.example.Model.Person;
import com.example.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.Set;

import static android.support.v7.appcompat.R.attr.icon;

/**
 * Created by Michael on 3/27/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private static MainActivity mainActivity;
    private static double lat;
    private static double lng;

    public static double getLng() {
        return lng;
    }

    public static void setLng(double lng) {
        MapFragment.lng = lng;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        MapFragment.lat = lat;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout footer = (LinearLayout) mView.findViewById(R.id.footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start new activity
                makeToast("It clicked");
                startPersonActivity();



            }
        });
        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                //load the event info bases on what they event has
                String eventID = marker.getTitle(); // the title contains the ID
                //get the event info
                Event event = ClientModel.getInstance().events.get(eventID);
                Person person  = ClientModel.getInstance().people.get(event.getPersonID());
                //sets the current person so that the PersonView can get the person info
                ClientModel.getInstance().setCurrentPerson(person);
                String name = person.getFirstname() + " " + person.getLastname();
                String eventStr = event.getDescription() + ": " + event.getCity() +
                        ", " + event.getCountry() + " (" + event.getYear() + ")";
                //get the person info
                //this code below replaces the two fields with the right what ever info you give it
                changeFooterInfo(name,eventStr);
                //add an icon to the footer


                return true;
            }
        });
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        loadEventsIntoMap(googleMap);

        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(getLat(), getLng()))
                .zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar, menu);
    }
    public void loadEventsIntoMap(GoogleMap googleMap){
        //initilize the event and color list
        ClientModel.getInstance().generateColorandEventList();
        //adds each event to the map with corresponding color
        for(String key : ClientModel.getInstance().events.keySet()){
            String lat = ClientModel.getInstance().events.get(key).getLatitude();
            String lng = ClientModel.getInstance().events.get(key).getLongitude();
            float latitude = Float.parseFloat(lat);
            float longitude = Float.parseFloat(lng);
            String eventType = ClientModel.getInstance().events.get(key).getDescription();
            String eventID = ClientModel.getInstance().events.get(key).getEventID();
            float color = Float.parseFloat(ClientModel.getInstance().eventTypeAndColor.get(eventType));
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                    .position(new LatLng(latitude, longitude))
                    .title(eventID));
        }

    }
    private void changeFooterInfo(String name, String info){
        TextView personName = (TextView) mView.findViewById(R.id.person_name);
        personName.setText(name);
        TextView eventInfo = (TextView) mView.findViewById(R.id.event_and_location_and_year);
        eventInfo.setText(info);
        //attach the right icon here


    }
    private void makeToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
    private void startPersonActivity(){
        PersonActivity.setMainActivity(mainActivity);
        Intent intent = new Intent(mainActivity, PersonActivity.class);
        startActivity(intent);
    }

}
