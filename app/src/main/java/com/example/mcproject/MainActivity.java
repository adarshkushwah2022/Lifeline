package com.example.mcproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, leftAnim;
    TextView appName;
    ImageView logo_image;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private LocationRequest locationRequest;
    private CustomLocationHandler locationHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationHandler = new CustomLocationHandler(this);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);

        appName = findViewById(R.id.startScreenTxt);
        logo_image = (ImageView) findViewById(R.id.logo_image);
        logo_image.setAnimation(topAnim);
        appName.setAnimation(leftAnim);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        if(locationHandler.isGPSEnabled()){
            callMainActivity();
        }
        else
            locationHandler.turnOnGPS();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == -1){
                if (locationHandler.isGPSEnabled()) {
                    callMainActivity();
                }
            }
            else {
                Toast.makeText(MainActivity.this,"Turn on GPS to continue", Toast.LENGTH_SHORT).show();
                locationHandler.turnOnGPS();
            }
        }
    }

    private void callMainActivity(){
        String uid = sharedPreferences.getString(KEY_UID, null);
        if(uid != null){
            FirebaseDatabase.getInstance().getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, mainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}