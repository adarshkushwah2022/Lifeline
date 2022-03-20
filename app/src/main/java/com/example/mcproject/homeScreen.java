package com.example.mcproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeScreen extends Fragment {
    TextView tv;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private static final String KEY_USERNAME = "USERNAME";
    private SharedPreferences sharedPreferences;
    private  TextView txtViewGreeting;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String uid, username;
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
        txtViewGreeting = view.findViewById(R.id.txtViewGreeting);
        txtViewGreeting.setText("Welcome, " + username);
        tv=view.findViewById(R.id.textView6);
        return view;
    }
}