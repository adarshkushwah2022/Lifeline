package com.example.mcproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mcproject.sendNotification.Client;
import com.example.mcproject.sendNotification.Data;
import com.example.mcproject.sendNotification.MyResponse;
import com.example.mcproject.sendNotification.NotificationSender;
import com.example.mcproject.sendNotification.messagingAPIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  myAdapter extends RecyclerView.Adapter<myAdapter.holder> {
    int[] reds = {255,51,255,102,153,204,204,0};
    int[] greens = {255,255,153,255,76,255,0,51};
    int[] blues = {0,51,153,102,0,204,204,102};
    int colorCounter=0;
    String userID;
    Context context;
    List<notficationEntity> recievedNotificationsList;
    notificationListFragment fr;
    private messagingAPIService apiService;
    AppDatabase databaseObj;
    RecyclerView rcv;

    public myAdapter(List<notficationEntity> recievedNotificationsList, Context context, notificationListFragment fr,RecyclerView rcv) {
        this.context=context;
        this.recievedNotificationsList = recievedNotificationsList;
        this.fr=fr;
        this.rcv=rcv;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        databaseObj = AppDatabase.getDatabaseInstance(context);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  = inflater.inflate(R.layout.singlenotificationrow, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(messagingAPIService.class);

        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.holder holder, int position) {
        holder.userName.setText("Patient Name:   "+recievedNotificationsList.get(position).getUserName());
        holder.userNeed.setText(recievedNotificationsList.get(position).getRequirement()+"\nQuantity: "+recievedNotificationsList.get(position).getQuantity());
        holder.userTime.setText(recievedNotificationsList.get(position).getCurrentTime());

        userID = recievedNotificationsList.get(position).getUserID();
        holder.continueButtonLottieAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String usertoken = "";
                        usertoken = dataSnapshot.child("token").getValue(String.class);
                        Log.d("token", usertoken);
                        sendNotifications(usertoken);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                double userLatitude=recievedNotificationsList.get(position).getLatitude();
                double userLongitude=recievedNotificationsList.get(position).getLongitude();
                String name = recievedNotificationsList.get(position).getUserName();
                String mobile = recievedNotificationsList.get(position).getMobileNo();
                String requirement = recievedNotificationsList.get(position).getRequirement();
                String bloodGroup = recievedNotificationsList.get(position).getBloodGroup();
                String time = recievedNotificationsList.get(position).getCurrentTime();
                String quant = recievedNotificationsList.get(position).getQuantity();


//                Bundle d = new Bundle();
//                d.putDouble("patientLatitude",userLatitude);
//                d.putDouble("patientLongitude",userLongitude);
                Intent i = new Intent(context,DonorActivity.class);
                i.putExtra("patientLatitude",userLatitude);
                i.putExtra("patientLongitude",userLongitude);
                i.putExtra("name",name);
                i.putExtra("mobile",mobile);
                i.putExtra("requirement",requirement);
                i.putExtra("bloodGroup",bloodGroup);
                i.putExtra("time",time);
                i.putExtra("quantity",quant);
                context.startActivity(i);
            }
        });

        holder.rejectLottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    databaseObj.userDao().deleteNotificationById(recievedNotificationsList.get(position).getUserID());

//                    recievedNotificationsList.remove(position);
//                    rcv.removeViewAt(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, recievedNotificationsList.size());
//                    notifyDataSetChanged();
                    //fr.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuContainer,new notificationListFragment()).commit();
                    Toast.makeText(context, "Notification Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Intent i3 = new Intent(context,tempactivity.class);
                    context.startActivity(i3);
                    fr.getActivity().finish();
                }

                catch (Exception e)
                {

                }
            }
        });
    }
//    255 255 0
//    51 255 51
//    255 153 153
//    102 255 102
//    153 76 0 brown
//    204 255 204
//    204 0 204 dark pink
//   0 51 102 dark blue


    @Override
    public int getItemCount() {
        return recievedNotificationsList.size();
    }
    class holder extends RecyclerView.ViewHolder {
        TextView userName,userNeed,userTime;
        LottieAnimationView continueButtonLottieAnimation, rejectLottie;
        public holder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.singleNotificationUserName);
            userNeed = (TextView) itemView.findViewById(R.id.singleNotificationNeed);
            userTime = (TextView) itemView.findViewById(R.id.singleNotificationTime);
            continueButtonLottieAnimation=itemView.findViewById(R.id.continueButtonLottie);
            rejectLottie = itemView.findViewById(R.id.continueButtonLottie2);
//            txt.setBackgroundColor(Color.rgb(reds[colorCounter],greens[colorCounter],blues[colorCounter]));
//            if(reds[colorCounter]==153 || greens[colorCounter]==0 || blues[colorCounter]==102 ){
//                txt.setTextColor(Color.rgb(255,255,255));
//            }
//            colorCounter = (colorCounter + 1) % reds.length;
        }
    }

    public void sendNotifications(String usertoken) {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        Data data = new Data(true);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context.getApplicationContext(), "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}
