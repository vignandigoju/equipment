package com.example.application;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class managerFinJob extends Fragment {

    RecyclerView recyclerView;
    MymanJobHistoryAdaptar myJobHistoryAdaptar;
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
        myJobHistoryAdaptar = new MymanJobHistoryAdaptar(getContext(), list, false);
        recyclerView.setAdapter(myJobHistoryAdaptar);
        // Inflate the layout for this fragment




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    job_history_data jobHistoryData = dataSnapshot.getValue(job_history_data.class);
                    if (jobHistoryData != null && "completed".equalsIgnoreCase(jobHistoryData.getStatus())) {
                        list.add(jobHistoryData);
                    }
                }
                myJobHistoryAdaptar.notifyDataSetChanged();
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
}