package com.example.mcproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class mainMenuActivity extends AppCompatActivity {
    BottomNavigationView bmv;
    Boolean menuSet=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer,new homeScreen()).commit();

        bmv = findViewById(R.id.bottomNavigationView);
        bmv.setItemIconTintList(null);                  //used for showing original icon color in menu


        bmv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.home:
                        //Toast.makeText(getApplicationContext(), "Home Pressed", Toast.LENGTH_SHORT).show();
                        if(!menuSet){
                            replacefragment();
                        }
                        return true;
                    case R.id.profile:
                        //Toast.makeText(getApplicationContext(), "Profile Pressed", Toast.LENGTH_SHORT).show();
                        replacefragmentTwo();
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
    private void replacefragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer,new homeScreen()).commit();
    }
    private void replacefragmentTwo(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer,new profileScreen()).commit();
    }
}