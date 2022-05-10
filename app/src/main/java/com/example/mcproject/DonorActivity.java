package com.example.mcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DonorActivity extends AppCompatActivity {
    Button proceedToLiveLocationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        proceedToLiveLocationButton=findViewById(R.id.proceedButtonDonorActivity);
        proceedToLiveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DonorActivity.this,locationTrackActivity.class);
                startActivity(i);
            }
        });
    }
}