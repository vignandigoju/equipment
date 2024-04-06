package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class equipment extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<equipment_data> list;
    DatabaseReference databaseReference;
    MyEquipmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("Equipment");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyEquipmentAdapter(this, list);
        recyclerView.setAdapter(adapter);
        SearchView searchView = findViewById(R.id.searchView);
        ImageButton backButton = findViewById(R.id.backHome);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("filterText")) {
            String filterText = intent.getStringExtra("filterText");
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
            databaseReference.orderByValue().equalTo(filterText).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            String key = dataSnapshot.getKey();
                            String equipmentName = dataSnapshot.getValue(String.class);

                            // Check if the equipmentName is not null
                            if (equipmentName != null) {
                                list.add(new equipment_data(key));
                            }
                        } catch (DatabaseException e) {
                            Log.e("FirebaseError", "Error converting value", e);
                            Log.d("FirebaseError", "Snapshot value: " + dataSnapshot.getValue());
                        }
                    }
                    MyEquipmentAdapter adapter = (MyEquipmentAdapter) recyclerView.getAdapter();
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
                    Intent intent = new Intent(equipment.this, supervisor_home.class);
                    startActivity(intent);
                }
            });

            adapter.setOnItemClickListener(new MyEquipmentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(equipment_data equipmentData) {
                    // Handle item click, for example, navigate to the next activity
                    Intent intent = new Intent(equipment.this, equipment_details.class);
                    intent.putExtra("selectedEquipmentId", equipmentData.getequipmentName());
                    intent.putExtra("filterText",filterText);
                    startActivity(intent);
                }
            });

        }
    }
}