package com.example.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyJobHistoryAdaptar extends RecyclerView.Adapter<MyJobHistoryAdaptar.MyViewHolder> {

    private Context context;
    private ArrayList<job_history_data> list;
    private boolean isButtonVisible;
    private OnButtonClickListener onButtonClickListener;

    public MyJobHistoryAdaptar(Context context, ArrayList<job_history_data> list, boolean isButtonVisible) {
        this.context = context;
        this.list = list;
        this.isButtonVisible = isButtonVisible;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_history, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("AdapterLog", "onBindViewHolder called for position: " + position);
        job_history_data jobHistoryData = list.get(position);
        if (holder.jobID != null && jobHistoryData.getJobID() != null) {
            holder.jobID.setText(jobHistoryData.getJobID());
        }

        if (holder.jobType != null && jobHistoryData.getServiceType() != null) {
            holder.jobType.setText(jobHistoryData.getServiceType());
        }if (holder.worker != null) {
            holder.worker.setText(String.valueOf(jobHistoryData.getEmpId()));
        }if (holder.location != null) {
            holder.location.setText(String.valueOf(jobHistoryData.getLocation()));
        }

        if (holder.date != null && jobHistoryData.getDate() != null) {
            holder.date.setText(jobHistoryData.getDate());
        }

        if (holder.status != null && jobHistoryData.getStatus() != null) {
            holder.status.setText(jobHistoryData.getStatus());
        } if (holder.service != null && jobHistoryData.getServiceCharge() != null) {
            holder.service.setText(jobHistoryData.getServiceCharge());
        }

        if (holder.button17 != null) {
            holder.button17.setVisibility("pending".equalsIgnoreCase(jobHistoryData.getStatus()) ? View.VISIBLE : View.GONE);
            holder.button17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click here
                    if (onButtonClickListener != null) {
                        onButtonClickListener.onButtonClick(holder.getAdapterPosition());
                    }
                }
            });
        }

        Log.d("AdapterLog", "onBindViewHolder: Position - " + position);
    }

    @Override
    public int getItemCount() {
        Log.d("AdapterLog", "getItemCount: " + list.size());
        return list.size();
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView jobID, jobType, date, status,worker,location,service;
        Button button17;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            jobID = itemView.findViewById(R.id.jobID);
            jobType = itemView.findViewById(R.id.serviceTypeInput);
            date = itemView.findViewById(R.id.serviceChargeInput);
            status = itemView.findViewById(R.id.statusInput);
            button17 = itemView.findViewById(R.id.button17);
            worker = itemView.findViewById(R.id.workerInput);
            location = itemView.findViewById(R.id.locationI);
            service = itemView.findViewById(R.id.servicccccInput);

        }
    }
}
