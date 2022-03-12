package com.example.mcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, leftAnim;
    TextView appName;
    ImageView logo_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);

        appName = findViewById(R.id.startScreenTxt);
        logo_image = (ImageView) findViewById(R.id.logo_image);
        logo_image.setAnimation(topAnim);
        appName.setAnimation(leftAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something
                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3500);
    }
}