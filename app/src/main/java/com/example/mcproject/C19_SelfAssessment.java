package com.example.mcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class C19_SelfAssessment extends AppCompatActivity {

    private TextView symptomText;
    private LottieAnimationView nextBtn;
    private LottieAnimationView symptomAnimation;
    private RadioGroup yes_no;
    private String symptom_array[] = {"FEVER", "COUGH", "COLD", "HEADACHE", "RUNNING NOSE", "SHORTNESS OF BREATH", "SOUR THROAT"};
    private boolean markedSymptoms[] = new boolean[symptom_array.length];
    private int currentIndex = 0;
    private boolean yes_checked = false;
    private boolean radio_checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c19_self_assessment);
        symptomText = (TextView) findViewById(R.id.sa_textview);
        nextBtn = (LottieAnimationView) findViewById(R.id.next_btn);
        symptomAnimation = (LottieAnimationView) findViewById(R.id.sa_symptom_anim);
        yes_no = (RadioGroup) findViewById(R.id.yes_no_radio);
        yes_no.clearCheck();
        nextBtn.setVisibility(View.INVISIBLE);


        yes_no.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.radioButton_yes:
                        yes_checked = true;
                        radio_checked = true;
                        if( radio_checked)
                        {
                                nextBtn.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.radioButton_no:
                        yes_checked = false;
                        radio_checked = true;
                        if( radio_checked )
                        {
                                nextBtn.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        nextBtn.setVisibility(View.INVISIBLE);
                        break;
                }

            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex == symptom_array.length-1)
                {
                    // call second Activity
                    changeActivity();
                    updateSymptoms();

                }
                else {
                    updateSymptoms();
                }
                yes_no.clearCheck();
                if(currentIndex == symptom_array.length - 1)
                {
                    nextBtn.setAnimation(R.raw.sa_submit);
                }
            }
        });

    }

    private void updateSymptoms() {
        {
            if(yes_checked)
            {
                markedSymptoms[currentIndex] = true;
//                Toast.makeText(MainActivity.this, "You have "+numberOfSymptoms+" Covid-19 symptoms", Toast.LENGTH_SHORT).show();
            }
            else
            {
                markedSymptoms[currentIndex] = false;
            }
            currentIndex = (currentIndex + 1) % symptom_array.length;
            symptomText.setText(symptom_array[currentIndex]);
            String sym = "symptom_"+currentIndex;
            int resourceID = getResources().getIdentifier("symptom_"+currentIndex, "raw", getPackageName());
            symptomAnimation.setAnimation(resourceID);
            symptomAnimation.playAnimation();

        }
    }

    private void changeActivity() {
        Intent intent = new Intent(C19_SelfAssessment.this, C19_SelfAssessmentResult.class );
        intent.putExtra("markedSymptoms", markedSymptoms);
        intent.putExtra("symptom_array", symptom_array);
        nextBtn.setAnimation(R.raw.sa_next);
//        intent.putExtra("bool_symptom_array", bool_symptom_array);
        startActivity(intent);
        finish();
    }
}