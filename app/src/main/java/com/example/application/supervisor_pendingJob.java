package com.example.application;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class supervisor_pendingJob extends Fragment {

    RecyclerView recyclerView;
    MyJobHistoryAdaptar myJobHistoryAdaptar;
    DatabaseReference databaseReference;
    ArrayList<job_history_data> list;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_supervisor_pending_job, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("jobHistory");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myJobHistoryAdaptar = new MyJobHistoryAdaptar(getContext(), list, true);
        myJobHistoryAdaptar.setOnButtonClickListener(new MyJobHistoryAdaptar.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position) {
                // Handle the button click, and update the status and service charge in the database
                job_history_data clickedJob = list.get(position);
                showInputDialog(clickedJob.getJobID(), "Completed");
                Log.d("ClickedJob", "ClickedJob" + clickedJob.getJobID());
                Log.d("Update Status", "Button Clicked");
            }
        });
        recyclerView.setAdapter(myJobHistoryAdaptar);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    job_history_data jobHistoryData = dataSnapshot.getValue(job_history_data.class);
                    if (jobHistoryData != null && "pending".equalsIgnoreCase(jobHistoryData.getStatus())) {
                        list.add(jobHistoryData);
                    }
                }
                myJobHistoryAdaptar.notifyDataSetChanged();
                // Check if the list is empty
                if (list.isEmpty()) {
                    // Inflate a different layout
                    LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View emptyView = layoutInflater.inflate(R.layout.empty_layout, null);
                    ViewGroup parentViewGroup = (ViewGroup) recyclerView.getParent();
                    parentViewGroup.addView(emptyView, 0);
//                } else {
//                    // Remove the empty layout if it's already inflated
//                    ViewGroup parentViewGroup = (ViewGroup) recyclerView.getParent();
//                    View emptyView = parentViewGroup.getChildAt(0);
//                    if (emptyView != null && emptyView.getId() == R.id.empty_layout) {
//                        parentViewGroup.removeView(emptyView);
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


    private void updateStatusAndServiceChargeInDatabase(String jobID, String newStatus, String serviceCharge) {
        DatabaseReference jobHistoryRef = FirebaseDatabase.getInstance().getReference("portal").child("jobHistory");

        jobHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    job_history_data jobHistoryData = childSnapshot.getValue(job_history_data.class);

                    if (jobHistoryData != null && jobHistoryData.getJobID().equals(jobID)) {
                        // Update the status for the specific jobID
                        DatabaseReference specificJobRef = jobHistoryRef.child(childSnapshot.getKey());
                        specificJobRef.child("status").setValue(newStatus);
                        specificJobRef.child("serviceCharge").setValue(serviceCharge);


                        // Update the serviceCharge under a different reference
                        updateServiceChargeInDatabase(jobID, newStatus, serviceCharge);
                        Log.d("service", "Service charge has been called");

                        break; // Break the loop since the jobID is found
                    } else {
                        Log.d("Not found", "Job ID not found");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });
    }
    private void updateServiceChargeInDatabase(String jobID, String newStatus, String newServiceCharge) {
        DatabaseReference equipment2Ref = FirebaseDatabase.getInstance().getReference("portal").child("Equipment2");

        equipment2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()) {
                    String equipmentKey = equipmentSnapshot.getKey();
                    if (equipmentSnapshot.hasChild("job_histoy")) {
                        // Found the equipment node with the matching jobID
                        DatabaseReference jobHistoryRef = equipment2Ref.child(equipmentKey).child("job_history");
                        jobHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot jobHistorySnapshot : snapshot.getChildren()) {
                                    // Iterate through job history nodes to find the correct one
                                    job_history_data jobHistoryData = jobHistorySnapshot.getValue(job_history_data.class);
                                    if (jobHistoryData != null && jobHistoryData.getJobID().equals(jobID)) {
                                        // Found the correct job history node
                                        DatabaseReference specificJobRef = jobHistoryRef.child(jobHistorySnapshot.getKey()).child("serviceCharge");
                                        specificJobRef.setValue(newServiceCharge);
                                        DatabaseReference status = jobHistoryRef.child(jobHistorySnapshot.getKey());
                                        status.child("status").setValue(newStatus);
                                        break;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });
    }

    private void showInputDialog(String jobID, String newStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Service Charge");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String serviceCharge = input.getText().toString();
                updateStatusAndServiceChargeInDatabase(jobID, newStatus, serviceCharge);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
