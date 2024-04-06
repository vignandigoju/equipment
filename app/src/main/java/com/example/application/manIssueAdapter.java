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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class manIssueAdapter extends RecyclerView.Adapter<manIssueAdapter.MyViewHolder> {

    Context context;
    ArrayList<man_issues_data> list;
    DatabaseReference databaseReference;
    private OnButtonClickListener onButtonClickListener;

    public manIssueAdapter(Context context, ArrayList<man_issues_data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.issues,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference("portal");
        man_issues_data manIssuesData = list.get(position);
        holder.equipmentId.setText(String.valueOf(manIssuesData.getEquipmentId()));
        holder.equipmentName.setText(manIssuesData.getEquipmentName());
        holder.issueType.setText(manIssuesData.getIssueType());
        holder.comment.setText(manIssuesData.getComment());
        holder.location.setText(manIssuesData.getLocation());
        holder.status.setText(manIssuesData.getStatus());

        if ("completed".equalsIgnoreCase(manIssuesData.getStatus())) {
            holder.button17.setVisibility(View.GONE);
        } else {
            holder.button17.setVisibility(View.VISIBLE);
            holder.button17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (onButtonClickListener != null) {
                        onButtonClickListener.onButtonClick(clickedPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView equipmentId, equipmentName, issueType, comment,location, status;
        Button button17;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            equipmentId = itemView.findViewById(R.id.dateInput);
            equipmentName = itemView.findViewById(R.id.serviceTypeInput);
            issueType = itemView.findViewById(R.id.issueTypeInput);
            comment = itemView.findViewById(R.id.serviceChargeInput);
            location = itemView.findViewById(R.id.locationInput);
            status = itemView.findViewById(R.id.statusInput);
            button17 = itemView.findViewById(R.id.button17);
        }
    }

    public interface OnButtonClickListener {
        void onButtonClick(int position);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }
}
