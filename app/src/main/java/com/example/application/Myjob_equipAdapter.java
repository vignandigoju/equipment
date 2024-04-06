package com.example.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Myjob_equipAdapter extends RecyclerView.Adapter<Myjob_equipAdapter.MyViewHolder> {

    Context context;
    ArrayList<job_equp_data> list;

    public Myjob_equipAdapter(Context context, ArrayList<job_equp_data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_equip,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        job_equp_data jobEqupData = list.get(position);
        holder.date.setText(jobEqupData.getDate());
        holder.serviceType.setText(jobEqupData.getServiceType());
        holder.serviceCharge.setText(jobEqupData.getServiceCharge());
        holder.status.setText(jobEqupData.getStatus());

        Log.d("AdapterLog", "onBindViewHolder: Position - " + position);

    }

    @Override
    public int getItemCount() {
        Log.d("AdapterLog", "getItemCount: " + list.size());
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date, serviceType, serviceCharge, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateInput);
            serviceType = itemView.findViewById(R.id.serviceTypeInput);
            serviceCharge = itemView.findViewById(R.id.serviceChargeInput);
            status = itemView.findViewById(R.id.statusInput);

        }
    }
}
