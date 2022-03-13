package com.example.mcproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class mainMenuActivity extends AppCompatActivity {
    BottomNavigationView bmv;
    Boolean menuSet=true;
    LottieAnimationView lottieAnimationView;
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
                        Intent i=new Intent(mainMenuActivity.this,CovidCasesActivity.class);
                         startActivity(i);
                        menuSet=false;
                        return true;
                }
                return false;
            }
        });
//        lottieAnimationView=findViewById(R.id.knowmore);
//        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(mainMenuActivity.this,CovidCasesActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }
    private void replacefragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer,new homeScreen()).commit();
    }
    private void replacefragmentTwo(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer,new profileScreen()).commit();
    }
}