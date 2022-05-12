package com.example.mcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class DonorActivity extends AppCompatActivity {
    LottieAnimationView proceedToLiveLocationButton;
    Button callButton;
    TextView name, requirement, time, bloodGroup, mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        double patientLatitude = getIntent().getDoubleExtra("patientLatitude",0);
        double patientLongitude = getIntent().getDoubleExtra("patientLongitude",0);
        String name1 = getIntent().getStringExtra("name");
        String mobile1 = getIntent().getStringExtra("mobile");
        String requirement1 = getIntent().getStringExtra("requirement");
        String bloodGroup1 = getIntent().getStringExtra("bloodGroup");
        String time1 = getIntent().getStringExtra("time");


        Toast.makeText(this, ""+patientLatitude+" : "+patientLongitude, Toast.LENGTH_SHORT).show();
        proceedToLiveLocationButton=findViewById(R.id.proceedButtonDonorActivity);
        name = findViewById(R.id.textView15);
        requirement = findViewById(R.id.textView22);
        time = findViewById(R.id.textView24);
        bloodGroup = findViewById(R.id.textView17);
        mobile = findViewById(R.id.textView19);
        callButton =findViewById(R.id.button5);

        name.setText(name1);
        mobile.setText(mobile1);
        requirement.setText(requirement1);
        bloodGroup.setText(bloodGroup1);
        time.setText(time1);

        proceedToLiveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i8 = new Intent(DonorActivity.this,locationTrackActivity.class);
                i8.putExtra("patientLatitude",patientLatitude);
                i8.putExtra("patientLongitude",patientLongitude);

                startActivity(i8);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String s = "tel:"+(String) mobile1;
                intent.setData(Uri.parse(s));
                startActivity(intent);
            }
        });
    }
}