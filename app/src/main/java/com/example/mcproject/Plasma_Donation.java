package com.example.mcproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Plasma_Donation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Plasma_Donation extends Fragment implements AdapterView.OnItemSelectedListener{

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
    EditText name, city, quantity, bloodGroup,mobileNo, email;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude, longitude;
    String uid;
    static ArrayList<User> users = new ArrayList<User>();
    List<String> nearestUsers;
    Spinner plasmaDonationBloodGroupSpinner;
    String current_blood_group="A";
    public static String[] bloodGroupArray = {"A", "B", "AB", "O"};
    AlertDialog.Builder builder;
    private String time_path = "time_data_plasma.txt";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Plasma_Donation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Plasma_Donation.
     */
    // TODO: Rename and change types and number of parameters
    public static Plasma_Donation newInstance(String param1, String param2) {
        Plasma_Donation fragment = new Plasma_Donation();
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
        View view =  inflater.inflate(R.layout.fragment_plasma__donation, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(KEY_UID, null);
        submitBtn = view.findViewById(R.id.plasmaDonationSubmitButton);
        clearBtn = view.findViewById(R.id.plasmaDonationClearButton);
        plasmaDonationBloodGroupSpinner = view.findViewById(R.id.plasmaDonationBloodGroupSpinner);
        name = view.findViewById(R.id.plasmaDonationName);
        mobileNo = view.findViewById(R.id.plasmaDonationMobile);
        quantity = view.findViewById(R.id.plasmaDonationQuantity);
        city = view.findViewById(R.id.plasmaDonationCity);
        email = view.findViewById(R.id.plasmaDonationEmail);
        builder = new AlertDialog.Builder(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,bloodGroupArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plasmaDonationBloodGroupSpinner.setAdapter(adapter);
        plasmaDonationBloodGroupSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        File dir = getActivity().getFilesDir();
        File file = new File(dir, time_path);
        if (file.exists()) {
            String stored_time = readTimeData();
            String cur_timestamp = getCurrentTimeStamp();
            long diff_time = findDifference(cur_timestamp, stored_time)[0];
            long diff_min = findDifference(cur_timestamp, stored_time)[1];
            long diff_days = findDifference(cur_timestamp, stored_time)[2];
            Log.d("TAG", "onCreateView: " + diff_min + " " + diff_days);
            if (diff_days == 0 && diff_min <= 30) {
                submitBtn.setEnabled(false);
                submitBtn.setBackgroundColor(Color.GRAY);
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("User has already sent a request for plasma donation, Kindly try to submit another request after " + (30 - diff_min) + " mins. Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().finish();
                                Toast.makeText(getContext(), "You have pressed YES",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getContext(), "You have pressed NO",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Plasma Request Form");
                alert.show();
            } else {
                submitBtn.setEnabled(true);
                submitBtn.setBackgroundResource(R.drawable.background_button1);
            }
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date mDate = new Date();
                String timestamp2 = getCurrentTimeStamp();
                saveTime(timestamp2);
                Log.d("TAG", "onClick: " + timestamp2);
                if(!validate()){
                    return;
                }
                Log.d("uid", "onClick: "+users.size());
                nearestUsers = new ArrayList<>();
                nearestUsers.clear();
                for(int i=0; i<users.size(); i++)
                {
                    User temp = users.get(i);
                    Log.d("uid", "onClick: "+temp.getUID());

                    if(temp.getUID().equals(uid)) {
                        Log.d("uid", "onClick: "+temp.getUID()+"   "+uid);
                        continue;
                    }
                    else
                    {
                        double dist = calculateDistance(Double.valueOf(temp.getLatitude()), Double.valueOf(temp.getLongitude()), latitude, longitude);
                        Log.d("uid", "onClick: "+dist);

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
                Toast.makeText(getContext(),"Notification Successfully Sent to "+nearestUsers.size()+" donors within 20 KM distance from you!!", Toast.LENGTH_LONG).show();
                submitBtn.setEnabled(false);
                submitBtn.setBackgroundColor(Color.GRAY);
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
        String userID;

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        Data data = new Data(name_t, current_blood_group,mobile_t,quantity_t, "Plasma", userID, latitude, longitude);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (parent.getId())
        {
            case R.id.bloodGroupSpinner:

                switch (position) {
                    case 0:
                        current_blood_group = bloodGroupArray[0];
                        break;
                    case 1:
                        current_blood_group = bloodGroupArray[1];
                        break;
                    case 2:
                        current_blood_group = bloodGroupArray[2];
                        break;
                    case 3:
                        current_blood_group = bloodGroupArray[3];
                        break;
                }
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private String readTimeData() {
        String Time_Path = time_path;
        String result = "";
        try {
            FileInputStream fin = getContext().openFileInput(Time_Path);
            int a;
            StringBuilder output = new StringBuilder();
            while ((a = fin.read()) != -1) {
                output.append((char) a);
            }
            result = output.toString();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    private void saveTime(String temp) {
        try {
            FileOutputStream fos = getContext().openFileOutput(time_path, Context.MODE_PRIVATE);
            String data = temp;
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    static long[]
    findDifference(String start_date,
                   String end_date) {
        long difference_In_Time[]={0,0,0};
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss");
        try {


            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            difference_In_Time[0]
                    = d1.getTime() - d2.getTime();
            difference_In_Time[1]
                    = (difference_In_Time[0] / (1000 * 60)) % 60;
            difference_In_Time[2]= (difference_In_Time[0] / (1000 * 60 * 60 * 24)) % 365;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return difference_In_Time;
    }


}