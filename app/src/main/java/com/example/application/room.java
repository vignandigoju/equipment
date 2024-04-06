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
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class room extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<room_data> list;
    DatabaseReference databaseReference;
    MyRoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);


        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("room");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRoomAdapter(this, list);
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
                        String roomName = dataSnapshot.getValue(String.class);
                        if (roomName != null) {
                            list.add(new room_data(roomName));
                        } else {
                            Log.e("FirebaseError", "Received null value for room_data");
                        }
                    } catch (DatabaseException e) {
                        // Log the error and check the value causing the issue
                        Log.e("FirebaseError", "Error converting value", e);
                        Log.d("FirebaseError", "Snapshot value: " + dataSnapshot.getValue());
                    }
                }
                MyRoomAdapter adapter = (MyRoomAdapter) recyclerView.getAdapter();
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
                Intent intent = new Intent(room.this, supervisor_home.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new MyRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(room_data roomData) {
                // Handle item click, for example, navigate to the next activity
                String filterText = getIntent().getStringExtra("filterText");
                Intent intent = new Intent(room.this, equipment.class);
                // Pass data to the next activity if needed
                intent.putExtra("filterText", filterText);
                startActivity(intent);
            }
        });

    }
}