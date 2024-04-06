package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class floor extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<floor_data> list;
    DatabaseReference databaseReference;
    MyFloorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);


        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("floor");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyFloorAdapter(this, list);
        recyclerView.setAdapter(adapter);
        SearchView searchView = findViewById(R.id.searchView);
        ImageButton backButton = findViewById(R.id.backHome);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    try {
                        String floorName = dataSnapshot.getValue(String.class);
                    if (floorName != null) {
                        list.add(new floor_data(floorName));
                    } else {
                        Log.e("FirebaseError", "Received null value for floor_data");
                    }
                    } catch (DatabaseException e) {
                        // Log the error and check the value causing the issue
                        Log.e("FirebaseError", "Error converting value", e);
                        Log.d("FirebaseError", "Snapshot value: " + dataSnapshot.getValue());
                    }
                }
                MyFloorAdapter adapter = (MyFloorAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.getOriginalList().clear();
                    adapter.getOriginalList().addAll(list);
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
                Log.d("DataChange", "Original List Size: " + adapter.getOriginalList().size());
                Log.d("DataChange", "List Size: " + list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to manager_home activity
                Intent intent = new Intent(floor.this, supervisor_home.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new MyFloorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(floor_data floorData) {
                String filterText = getIntent().getStringExtra("filterText");
                // Handle item click, for example, navigate to the next activity
                Intent intent = new Intent(floor.this, room.class);
                // Pass data to the next activity if needed
                intent.putExtra("filterText", filterText);
                startActivity(intent);
            }
        });

    }
}