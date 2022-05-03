package com.example.mcproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeScreen extends Fragment {
    TextView tv;
    CardView knowMore,bloodDonate,plasmaDonate,oxygenCylinderDonate,covidStats, selfAssessment;
    LinearLayout linearLayout;
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private static final String KEY_USERNAME = "USERNAME";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String uid, username;
    private SharedPreferences sharedPreferences;
    private  TextView txtViewGreeting;


    public homeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static homeScreen newInstance(String param1, String param2) {
        homeScreen fragment = new homeScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(KEY_USERNAME,null);
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home_screen, container, false);
        knowMore = view.findViewById(R.id.homeScreenCardView8);
        bloodDonate = view.findViewById(R.id.HomeScreenCardView2);
        plasmaDonate = view.findViewById(R.id.HomeScreenCardView1);
        oxygenCylinderDonate = view.findViewById(R.id.HomeScreenCardView4);
        covidStats= view.findViewById(R.id.cardView5);
        selfAssessment = view.findViewById(R.id.cardView7);
        txtViewGreeting = view.findViewById(R.id.txtViewGreeting);
        txtViewGreeting.setText("Welcome, " + username);
        knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainMenuContainer,new knowMoreScreen())
                             .addToBackStack(null)
                             .commit();
            }
        });

        bloodDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainMenuContainer,new Blood_donation())
                        .addToBackStack(null)
                        .commit();
            }
        });

        plasmaDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainMenuContainer,new Plasma_Donation())
                        .addToBackStack(null)
                        .commit();
            }
        });

        oxygenCylinderDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainMenuContainer,new Oxygen_Cylinder_donation())
                        .addToBackStack(null)
                        .commit();
            }
        });

        covidStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),CovidCasesActivity.class);
                startActivity(i);
            }
        });

        selfAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),C19_SelfAssessment.class);
                startActivity(i);
            }
        });

        return view;
    }

}
