package com.example.mcproject;

import static android.view.View.GONE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeScreen extends Fragment {
    TextView tv;
    CardView knowMore,bloodDonate,plasmaDonate,oxygenCylinderDonate,covidStats, selfAssessment;
    LinearLayout linearLayout;
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private static final String KEY_USERNAME = "USERNAME";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String uid, username;
    private SharedPreferences sharedPreferences;
    private  TextView txtViewGreeting;
    private CustomLocationHandler locationHandler;
    private LocationRequest locationRequest;


    LocationManager locationManager;
    LocationListener locationListener;

    public homeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static homeScreen newInstance(String param1, String param2) {
        homeScreen fragment = new homeScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(KEY_USERNAME,null);
        uid = sharedPreferences.getString(KEY_UID, null);
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home_screen, container, false);
        knowMore = view.findViewById(R.id.homeScreenCardView8);
        bloodDonate = view.findViewById(R.id.HomeScreenCardView2);
        plasmaDonate = view.findViewById(R.id.HomeScreenCardView1);
        oxygenCylinderDonate = view.findViewById(R.id.HomeScreenCardView4);
        covidStats= view.findViewById(R.id.cardView5);
        selfAssessment = view.findViewById(R.id.cardView7);
        txtViewGreeting = view.findViewById(R.id.txtViewGreeting);
        txtViewGreeting.setText("Welcome, " + username);
        locationHandler = new CustomLocationHandler(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        getCurrentLocation();
        knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainMenuContainer,new knowMoreScreen())
                        .addToBackStack(null)
                        .commit();
            }
        });

        bloodDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainMenuContainer,new Blood_donation())
                        .addToBackStack(null)
                        .commit();
            }
        });

        plasmaDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainMenuContainer,new Plasma_Donation())
                        .addToBackStack(null)
                        .commit();
            }
        });

        oxygenCylinderDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainMenuContainer,new Oxygen_Cylinder_donation())
                        .addToBackStack(null)
                        .commit();
            }
        });

        covidStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),CovidCasesActivity.class);
                startActivity(i);
            }
        });

        selfAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),C19_SelfAssessment.class);
                startActivity(i);
            }
        });

        return view;
    }
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationListener = new MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, locationListener);


                if (locationHandler.isGPSEnabled()) {

                    Location location;
                    if (locationManager != null) {
                        location = getLastKnownLocation();

                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Map<String, Object> userUpdates = new HashMap<>();
                            userUpdates.put(uid + '/' + "latitude", Double.toString(latitude));
                            userUpdates.put(uid + '/' + "longitude", Double.toString(longitude));
                            FirebaseDatabase.getInstance().getReference("Users").updateChildren(userUpdates);

                        }
                    }

                } else {
                    locationHandler.turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private Location getLastKnownLocation() {

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location loc) {


        }

        @Override
        public void onFlushComplete(int requestCode) {
            LocationListener.super.onFlushComplete(requestCode);
        }


        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }
}

