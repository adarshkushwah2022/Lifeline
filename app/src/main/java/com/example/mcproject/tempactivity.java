package com.example.mcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class tempactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempactivity);
        if(getIntent().getExtras()!=null){
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        }
    }
}