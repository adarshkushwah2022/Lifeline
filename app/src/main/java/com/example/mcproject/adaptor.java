package com.example.mcproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adaptor extends RecyclerView.Adapter<adaptor.vholder> {
    ArrayList<Covid_Model>data;
    Context context;
    public adaptor(Context context,ArrayList<Covid_Model>data) {
        this.data=data;
        this.context=context;
    }
    public vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.singlerow_covidcases,parent,false);
        return new vholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull vholder holder, int position) {
        holder.state.setText(data.get(position).getState_name());
        holder.active_case.setText(data.get(position).getActive_case());
        holder.total_case.setText(data.get(position).getTotal_case());
        holder.total_death.setText(data.get(position).getTotal_deaths());
        holder.recovery.setText(data.get(position).getRecovery());
    }

    @Override
    public int getItemCount() {
      //  Toast.makeText(context, ""+data.size(), Toast.LENGTH_SHORT).show();
        return data.size();
    }

    static class vholder extends RecyclerView.ViewHolder{
        TextView state,active_case,total_death,total_case,recovery;

        public vholder(@NonNull View itemView) {
            super(itemView);
            state=itemView.findViewById(R.id.singleNotificationUserName);
            active_case=itemView.findViewById(R.id.singleNotificationTime);
            total_case=itemView.findViewById(R.id.singleNotificationNeed);
            total_death=itemView.findViewById(R.id.total_death);
            recovery=itemView.findViewById(R.id.total_recovery);
        }
    }
}
