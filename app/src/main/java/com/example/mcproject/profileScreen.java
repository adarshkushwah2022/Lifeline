package com.example.mcproject;

import static android.view.View.GONE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileScreen extends Fragment{
    TextView profileScreenUserName, profileScreenContactNumber, profileScreenBloodGroup, profileScreenAddress,profileScreenPinCode,profileScreenMainName;
    Button editDetailsButton, signOutButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_FULLNAME = "FULLNAME";
    private static final String KEY_CONTACT = "CONTACTNO";
    private static final String KEY_BLOODGROUP = "BLOODGROUP";
    private static final String KEY_ADDRESS = "ADDRESS";
    private static final String KEY_PINCODE = "PINCODE";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sharedPreferences;
    private CustomLocationHandler locationHandler;
    private LocationRequest locationRequest;
    private ProgressBar progressBar;
    private View mView;
    public profileScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static profileScreen newInstance(String param1, String param2) {
        profileScreen fragment = new profileScreen();
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
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_profile_screen, container, false);
        profileScreenUserName =mView.findViewById(R.id.profileScreenEditText1);
        profileScreenContactNumber =mView.findViewById(R.id.profileScreenEditText2);
        profileScreenBloodGroup =mView.findViewById(R.id.profileScreenEditText3);
        profileScreenAddress =mView.findViewById(R.id.profileScreenEditText4);
        profileScreenPinCode=mView.findViewById(R.id.profileScreenEditText5);
        editDetailsButton=mView.findViewById(R.id.profileScreenEditDetailButton);
        signOutButton = mView.findViewById(R.id.profileScreenSignOutButton);
        profileScreenMainName = mView.findViewById(R.id.profileScreenMainName);
        progressBar = mView.findViewById(R.id.progressBar);
        locationHandler = new CustomLocationHandler(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        activeDeactiveDetails(false);
        fillProfile();
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editDetailsButton.getText().toString().equals("Edit Details")){
                    activeDeactiveDetails(true);
                    editDetailsButton.setText(R.string.save_details);
                }
                else{
                    activeDeactiveDetails(false);
                    editDetailsButton.setText(R.string.edit_details);
                    saveDetails();
                }
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                startActivity(intent);
            }
        });
        profileScreenAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationHandler.isGPSEnabled()){
                    progressBar.setVisibility(View.VISIBLE);
                    getCurrentLocation();
                }
                else
                    locationHandler.turnOnGPS();
            }
        });
        return  mView;
    }

    private void saveDetails() {
        String uid = sharedPreferences.getString(KEY_UID, null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FULLNAME, profileScreenUserName.getText().toString());
        editor.putString(KEY_CONTACT, profileScreenContactNumber.getText().toString());
        editor.putString(KEY_BLOODGROUP, profileScreenBloodGroup.getText().toString());
        editor.apply();
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put(uid + '/' + "fullname", sharedPreferences.getString(KEY_FULLNAME,null));
        userUpdates.put(uid + '/' + "contactNumber", sharedPreferences.getString(KEY_CONTACT,null));
        userUpdates.put(uid + '/' + "bloodGroup", sharedPreferences.getString(KEY_BLOODGROUP,null));
        userUpdates.put(uid + '/' + "address", sharedPreferences.getString(KEY_ADDRESS,null));
        userUpdates.put(uid + '/' + "pincode", sharedPreferences.getString(KEY_PINCODE,null));
        FirebaseDatabase.getInstance().getReference("Users").updateChildren(userUpdates);
    }

    private void activeDeactiveDetails(Boolean action){
        profileScreenAddress.setEnabled(action);
        profileScreenUserName.setEnabled(action);
        profileScreenBloodGroup.setEnabled(action);
        profileScreenContactNumber.setEnabled(action);
    }
    
    private void fillProfile(){
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String name  = sharedPreferences.getString(KEY_FULLNAME, null);
        String contact = sharedPreferences.getString(KEY_CONTACT, null);
        String bloodGroup = sharedPreferences.getString(KEY_BLOODGROUP, null);
        String address = sharedPreferences.getString(KEY_ADDRESS, null);
        String pincode = sharedPreferences.getString(KEY_PINCODE,null);
        profileScreenMainName.setText(username);
        if(name != null)
            profileScreenUserName.setText(name);
        if(contact != null)
            profileScreenContactNumber.setText(contact);
        if(bloodGroup != null)
            profileScreenBloodGroup.setText(bloodGroup);
        if(address != null){
            profileScreenAddress.setText(address);
            profileScreenPinCode.setText(pincode);
        }
        else{
            if(locationHandler.isGPSEnabled()){
                progressBar.setVisibility(View.VISIBLE);
                getCurrentLocation();
            }
            else
                locationHandler.turnOnGPS();
        }

    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (locationHandler.isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(getActivity())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(getActivity())
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        String strAdd = "";
                                        String pincode = "";
                                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                            if (addresses != null) {
                                                Address returnedAddress = addresses.get(0);
                                                pincode = addresses.get(0).getPostalCode();
                                                StringBuilder strReturnedAddress = new StringBuilder("");

                                                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                                                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                                                }
                                                strAdd = strReturnedAddress.toString();
                                                profileScreenAddress.setText(strAdd);
                                                profileScreenPinCode.setText(pincode);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(KEY_ADDRESS, strAdd);
                                                editor.putString(KEY_PINCODE, pincode);
                                                editor.apply();
                                                progressBar.setVisibility(GONE);
                                            } else {
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    locationHandler.turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
}