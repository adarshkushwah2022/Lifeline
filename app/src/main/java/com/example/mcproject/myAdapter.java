package com.example.mcproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

public class myAdapter extends RecyclerView.Adapter<myAdapter.holder> {
    int[] reds = {255,51,255,102,153,204,204,0};
    int[] greens = {255,255,153,255,76,255,0,51};
    int[] blues = {0,51,153,102,0,204,204,102};
    int colorCounter=0;
    Context context;
    String[] notificationNumberArray;
    notificationListFragment fr;
    public myAdapter(String[] newsNumberArray,Context context,notificationListFragment fr) {
        this.context=context;
        this.notificationNumberArray = newsNumberArray;
        this.fr=fr;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  = inflater.inflate(R.layout.singlenotificationrow, parent, false);

        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.holder holder, int position) {
        holder.userName.setText(notificationNumberArray[position]);
        holder.userNeed.setText(notificationNumberArray[position]);
        holder.userTime.setText(notificationNumberArray[position]);

        holder.continueButtonLottieAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,DonorActivity.class);
                context.startActivity(i);
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
        return notificationNumberArray.length;
    }
    class holder extends RecyclerView.ViewHolder {
        TextView userName,userNeed,userTime;
        LottieAnimationView continueButtonLottieAnimation;
        public holder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.singleNotificationUserName);
            userNeed = (TextView) itemView.findViewById(R.id.singleNotificationNeed);
            userTime = (TextView) itemView.findViewById(R.id.singleNotificationTime);
            continueButtonLottieAnimation=itemView.findViewById(R.id.continueButtonLottie);
//            txt.setBackgroundColor(Color.rgb(reds[colorCounter],greens[colorCounter],blues[colorCounter]));
//            if(reds[colorCounter]==153 || greens[colorCounter]==0 || blues[colorCounter]==102 ){
//                txt.setTextColor(Color.rgb(255,255,255));
//            }
//            colorCounter = (colorCounter + 1) % reds.length;
        }
    }
}
