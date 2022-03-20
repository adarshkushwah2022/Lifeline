package com.example.mcproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class mainMenuActivity extends AppCompatActivity {
    BottomNavigationView bmv;
    Boolean menuSet=true;
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private static final String KEY_USERNAME = "USERNAME";
    private SharedPreferences sharedPreferences;
    private CustomLocationHandler locationHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        loadHomeScreen();
        bmv = findViewById(R.id.bottomNavigationView);
        bmv.setItemIconTintList(null);
        locationHandler = new CustomLocationHandler(this);
        bmv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.home:
                        if(!menuSet){
                            loadHomeScreen();
                        }
                        return true;
                    case R.id.profile:
                        loadProfile();
                        menuSet=false;
                        return true;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(), "Settings Pressed", Toast.LENGTH_SHORT).show();
                        menuSet=false;
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == 0){
                locationHandler.turnOnGPS();
            }
        }
    }


    private void loadHomeScreen(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer, new homeScreen()).commit();
    }
    private void loadProfile(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer,new profileScreen()).commit();
    }
}