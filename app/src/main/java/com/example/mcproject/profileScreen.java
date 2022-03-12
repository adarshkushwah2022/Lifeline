package com.example.mcproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileScreen extends Fragment {
    TextView profileScreenUserName, profileScreenContactNumber, profileScreenBloodGroup, profileScreenAddress,profileScreenPinCode;
        Button editDetailsButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static profileScreen newInstance(String param1, String param2) {
        profileScreen fragment = new profileScreen();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_screen, container, false);
        profileScreenUserName =view.findViewById(R.id.profileScreenEditText1);
        profileScreenContactNumber =view.findViewById(R.id.profileScreenEditText2);
        profileScreenBloodGroup =view.findViewById(R.id.profileScreenEditText3);
        profileScreenAddress =view.findViewById(R.id.profileScreenEditText4);
        profileScreenPinCode=view.findViewById(R.id.profileScreenEditText5);
        editDetailsButton=view.findViewById(R.id.profileScreenEditDetailButton);

        activeDeactiveDetails(false);

        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeDeactiveDetails(true);
                editDetailsButton.setText("Save Details");
            }
        });

        return  view;
    }

    private void activeDeactiveDetails(Boolean action){
        profileScreenAddress.setEnabled(action);
        profileScreenUserName.setEnabled(action);
        profileScreenBloodGroup.setEnabled(action);
        profileScreenContactNumber.setEnabled(action);
        profileScreenPinCode.setEnabled(action);
    }
}