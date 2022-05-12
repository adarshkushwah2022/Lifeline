package com.example.mcproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class locationTrackActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MarkerOptions needHelpLocation;
    double patientLatitude,patientLongitude;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_track);
        patientLatitude = getIntent().getDoubleExtra("patientLatitude",0);
        patientLongitude = getIntent().getDoubleExtra("patientLongitude",0);
        needHelpLocation = new MarkerOptions().position(new LatLng(patientLatitude, patientLongitude)).title("Patient Location");
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(patientLatitude,patientLongitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Patient Location");
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        //mMap.addMarker(needHelpLocation);
    }

}