package com.example.mcproject;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mcproject.sendNotification.Client;
import com.example.mcproject.sendNotification.Data;
import com.example.mcproject.sendNotification.MyResponse;
import com.example.mcproject.sendNotification.NotificationSender;
import com.example.mcproject.sendNotification.Token;
import com.example.mcproject.sendNotification.messagingAPIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Oxygen_Cylinder_donation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Oxygen_Cylinder_donation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private messagingAPIService apiService;
    Button submitBtn, clearBtn;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private static final String KEY_TOKEN = "TOKEN";
    EditText name, city, quantity,mobileNo, email;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude, longitude;
    String uid;
    static ArrayList<User> users = new ArrayList<User>();
    List<String> nearestUsers;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Oxygen_Cylinder_donation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Oxygen_Cylinder_donation.
     */
    // TODO: Rename and change types and number of parameters
    public static Oxygen_Cylinder_donation newInstance(String param1, String param2) {
        Oxygen_Cylinder_donation fragment = new Oxygen_Cylinder_donation();
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
        apiService = Client.getClient("https://fcm.googleapis.com/").create(messagingAPIService.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_oxygen__cylinder_donation, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(KEY_UID, null);
        submitBtn = view.findViewById(R.id.o2SubmitButton);
        clearBtn = view.findViewById(R.id.o2ClearButton);
        name = view.findViewById(R.id.o2DonationName);
        mobileNo = view.findViewById(R.id.o2DonationMobile);
        quantity = view.findViewById(R.id.o2DonationQuantity);
        city = view.findViewById(R.id.o2DonationCity);
        email = view.findViewById(R.id.o2DonationEmail);
        getNearUsers();


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, locationListener);

        Location location;
        if (locationManager != null) {
            location = getLastKnownLocation();

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                mobileNo.setText("");
                quantity.setText("");
                city.setText("");
                email.setText("");
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }
                nearestUsers = new ArrayList<>();
                nearestUsers.clear();
                for(int i=0; i<users.size(); i++)
                {
                    User temp = users.get(i);

                    if(temp.getUID().equals(uid)) {
                        continue;
                    }
                    else
                    {
                        double dist = calculateDistance(Double.valueOf(temp.getLatitude()), Double.valueOf(temp.getLongitude()), latitude, longitude);
                        if(dist < 10.0)
                        {
                            nearestUsers.add(temp.getUID());
                        }
                    }
                }

                for(int i=0; i<nearestUsers.size(); i++) {

                    FirebaseDatabase.getInstance().getReference().child("Users").child(nearestUsers.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String usertoken = "";
                            usertoken = dataSnapshot.child("token").getValue(String.class);
                            Log.d("token", usertoken);
                            sendNotifications(usertoken);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }
            }
        });

        UpdateToken();
        return view;
    }

    private boolean validate(){
        String name_ = name.getText().toString().trim();
        String city_ = city.getText().toString().trim();
        String quantity_ = quantity.getText().toString().trim();
        String mobile_ = mobileNo.getText().toString().trim();
        String email_ = email.getText().toString().trim();

        if(name_.isEmpty()){
            name.setError("Name is required");
            name.requestFocus();
            return false;
        }
        if(city_.isEmpty()){
            city.setError("City is required");
            city.requestFocus();
            return false;
        }
        if(quantity_.isEmpty()){
            quantity.setError("Quantity is required");
            quantity.requestFocus();
            return false;
        }
        if(!quantity_.matches("[0-9]+")){
            quantity.setError("Numeric value is required");
            quantity.requestFocus();
            return false;
        }
        if(mobile_.isEmpty()){
            mobileNo.setError("Mobile Number is required");
            mobileNo.requestFocus();
            return false;
        }
        if(!mobile_.matches("[0-9]+")){
            mobileNo.setError("Numeric value is required");
            mobileNo.requestFocus();
            return false;
        }
        if(email_.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_).matches()){
            email.setError("Please provide valid email");
            email.requestFocus();
            return false;
        }
        return true;
    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token").setValue(refreshToken);


    }


    public void sendNotifications(String usertoken) {

        String name_t = name.getText().toString();
        String mobile_t = mobileNo.getText().toString();
        String quantity_t = quantity.getText().toString();
        Double latitude=0.0, longitude=0.0;
        String userID;

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        Data data = new Data(name_t, "NA",mobile_t,quantity_t, "Oxygen", userID, latitude, longitude);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


    private Location getLastKnownLocation() {
//        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
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


    void getNearUsers()
    {
        users.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User post = postSnapshot.getValue(User.class);
                    post.setUID(postSnapshot.getKey());
                    users.add(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    double calculateDistance(double lat1, double long1, double lat2, double long2)
    {
        double r = 6371;
        double lon = long2 - long1;
        double lat = lat2 - lat1;
        double x = Math.pow(Math.sin(lat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(lon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(x));
        double dist = r*c;
        return dist;
    }
}