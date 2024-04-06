package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class manage_users extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    myLoginAdapter myLoginAdapter;
    ArrayList<login_data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("users");
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myLoginAdapter = new myLoginAdapter(this,list);
        recyclerView.setAdapter(myLoginAdapter);
         list= new ArrayList<>();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    login_data loginData = dataSnapshot.getValue(login_data.class);
                    if (loginData != null) {
                        list.add(loginData);
                    }
                }
                Log.d("ManageUsers", "Data retrieved from Firebase. Count: " + list.size());
                myLoginAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ManageUsers", "Error fetching data from Firebase: " + error.getMessage());
            }
        });


    }
}