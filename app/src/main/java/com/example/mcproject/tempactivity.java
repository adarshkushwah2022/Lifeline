package com.example.mcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class tempactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempactivity);
        Intent intent = new Intent(this, mainMenuActivity.class);
        intent.putExtra("callFromNotification", true);
        startActivity(intent);
        finish();
    }
}