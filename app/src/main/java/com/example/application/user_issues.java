package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_issues extends AppCompatActivity implements manIssueAdapter.OnButtonClickListener{

    RecyclerView recyclerView;

    manIssueAdapter manIssueAdapter;
    DatabaseReference databaseReference;
    ArrayList<man_issues_data> list;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_issues);




        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("Issues");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
//        myIssuesAdapter = new MyIssuesAdapter(this, list, true);
//        recyclerView.setAdapter(myIssuesAdapter);
        manIssueAdapter = new manIssueAdapter(this, list);
        recyclerView.setAdapter(manIssueAdapter);
        manIssueAdapter.setOnButtonClickListener(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    man_issues_data manIssuesData = dataSnapshot.getValue(man_issues_data.class);
                    list.add(manIssuesData);
                }
              manIssueAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
    public void onClick(View view) {
        // Create an Intent to start the new activity (ActivityAddSupervisor)
        Intent intent = new Intent(this, user_home.class);

        // Optionally, you can pass data to the new activity using intent.putExtra()
        // intent.putExtra("key", "value");

        // Start the new activity
        startActivity(intent);
    }
    @Override
    public void onButtonClick(int position) {
        // Get the specific equipment ID from the clicked item
        String equipmentId = list.get(position).getEquipmentId(); // Assuming getEquipmentId() returns the equipment ID
        Log.d("ButtonClick", "Equipment ID: " + equipmentId);

        // Find the issue node with matching equipmentId
        DatabaseReference issuesRef = databaseReference;
        issuesRef.orderByChild("equipmentId").equalTo(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the key of the matched issue node
                    String issueKey = snapshot.getKey();
                    Log.d("ButtonClick", "Issue Key: " + issueKey);

                    // Update the status under the matched issue node
                    DatabaseReference issueRef = issuesRef.child(issueKey).child("status");
                    issueRef.setValue("completed").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseUpdate", "Status updated successfully for issue with equipment ID: " + equipmentId);
                            } else {
                                Log.e("FirebaseUpdate", "Failed to update status for issue with equipment ID: " + equipmentId);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}