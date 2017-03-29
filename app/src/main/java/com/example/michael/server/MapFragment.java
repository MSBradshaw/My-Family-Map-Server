package com.example.michael.server;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Model.Event;
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

/**
 * Created by Michael on 3/27/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                //this code below replaces the two fields with the right what ever info you give it
                changeFooterInfo("Sir Charles","Hatching: A kingdom Under the Sea, (2005)");


                return true;
            }
        });
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502))
                .title("Statue Of Liberty"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502))
                .zoom(16).bearing(0).tilt(45).build();
        loadEventsIntoMap(googleMap);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }
    public void loadEventsIntoMap(GoogleMap googleMap){
        //initilize the event and color list
        ClientModel.getInstance().generateColorandEventList();
        //adds each event to the map with coorsiponding color
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
                    .title(eventType));
        }

    }
    private void changeFooterInfo(String name, String info){
        TextView personName = (TextView) mView.findViewById(R.id.person_name);
        personName.setText(name);
        TextView eventInfo = (TextView) mView.findViewById(R.id.event_and_location_and_year);
        eventInfo.setText(info);
    }
}
