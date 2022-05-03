package com.example.mcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class C19_SelfAssessmentResult extends AppCompatActivity {

    TextView positiveSymptoms;
    TextView negativeSymptoms;
    TextView report;
    String yesSymptoms = "";
    String noSymptoms = "";
    LottieAnimationView reportAnim;
    LottieAnimationView homeAnim;
    int count = 0;
    private boolean[] markedSymptoms;
    private String[] symptom_array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c19_self_assessment_result);

        positiveSymptoms = (TextView) findViewById(R.id.positiveSymptoms);
        negativeSymptoms = (TextView) findViewById(R.id.negativeSymptoms);
        reportAnim = (LottieAnimationView)findViewById(R.id.sa_symptom_anim);
        homeAnim = (LottieAnimationView)findViewById(R.id.sa_home_anim);
        report = (TextView)findViewById(R.id.sa_report);
        Intent intent = getIntent();
        markedSymptoms = intent.getBooleanArrayExtra("markedSymptoms");
        symptom_array = intent.getStringArrayExtra("symptom_array");
        count = countSymptoms(markedSymptoms);
        yesSymptoms = yesMarked(markedSymptoms, symptom_array);
        noSymptoms = noMarked(markedSymptoms, symptom_array);
        positiveSymptoms.setText(yesSymptoms);
        negativeSymptoms.setText(noSymptoms);

        if(count>2)
        {
            reportAnim.setAnimation(R.raw.yes_report);
            report.setText("YES");
            report.setTextColor(Color.parseColor("#E64A19"));
        }
        else
        {
            reportAnim.setAnimation(R.raw.no_report);
            report.setText("NO");
            report.setTextColor(Color.parseColor("#689F38"));
        }

        homeAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    String yesMarked(boolean[] mark, String[] arr)
    {
        int c = 1;
        String s = "";
        for(int i = 0; i<mark.length; i++)
        {
            if(mark[i])
            {
                s = s+ c +":  "+arr[i]+"\n";
                c++;
            }
        }
        return s;
    }

    String noMarked(boolean[] mark, String[] arr)
    {
        String s = "";
        int c = 1;
        for(int i = 0; i<mark.length; i++)
        {
            if(!mark[i])
            {
                s = s+ c +":  "+arr[i]+"\n";
                c++;
            }
        }
        return s;
    }



    int countSymptoms(boolean[] arr)
    {
        int c=0;
        for(int i=0; i< arr.length; i++)
        {
            if(arr[i])
            {
                c++;
            }
        }
        return c;
    }
}