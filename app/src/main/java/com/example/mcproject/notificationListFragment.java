package com.example.mcproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notificationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notificationListFragment extends Fragment {
    RecyclerView rcv;
    Context context;

    AppDatabase databaseObj;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public notificationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notificationListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static notificationListFragment newInstance(String param1, String param2) {
        notificationListFragment fragment = new notificationListFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        context = getContext();
        databaseObj = AppDatabase.getDatabaseInstance(context);
        rcv = view.findViewById(R.id.rr8);

        List<notficationEntity> recievedNotificationsList = databaseObj.userDao().getNotificationsList();
        Collections.reverse(recievedNotificationsList);

        rcv.setAdapter(new myAdapter(recievedNotificationsList,context,this));
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}