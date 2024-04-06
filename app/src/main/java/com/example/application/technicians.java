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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class technicians extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyTechniciansAdapter myTechniciansAdapter;
    ArrayList<technicians_data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technicians);
        String selectedEquipmentId = getIntent().getStringExtra("selectedEquipmentId");
        String filterText = getIntent().getStringExtra("filterText");
        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("workers");
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.searchView);
        list = new ArrayList<>();
        myTechniciansAdapter = new MyTechniciansAdapter(this, list);
        recyclerView.setAdapter(myTechniciansAdapter);
        ImageButton backButton = findViewById(R.id.backHome);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to manager_home activity
                Intent intent = new Intent(technicians.this, equipment_details.class);
                intent.putExtra("selectedEquipmentId", selectedEquipmentId);
                intent.putExtra("filterText", filterText);
                startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myTechniciansAdapter.getFilter().filter(newText);
                return true;
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    technicians_data techniciansData = dataSnapshot.getValue(technicians_data.class);
                    list.add(techniciansData);
                }
                Log.d("Filter", "Original List Size2 : " + list.size());
                myTechniciansAdapter.notifyDataSetChanged();
                MyTechniciansAdapter myTechniciansAdapter = (MyTechniciansAdapter) recyclerView.getAdapter();
                if (myTechniciansAdapter != null) {
                    myTechniciansAdapter.getOriginalList().clear();
                    myTechniciansAdapter.getOriginalList().addAll(list);
                    myTechniciansAdapter.notifyDataSetChanged();
                }
                myTechniciansAdapter.notifyDataSetChanged();
                Log.d("DataChange", "Original List Size: " + myTechniciansAdapter.getOriginalList().size());
                Log.d("DataChange", "List Size: " + list.size());
            }
                @Override
                public void onCancelled (@NonNull DatabaseError error){

            }
        });
        myTechniciansAdapter.setOnItemClickListener(new MyTechniciansAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(technicians_data techniciansData) {
                Log.d("Filter", "Item Clicked" + techniciansData.getName());
                // Handle item click, for example, navigate to the next activity
                String selectedEquipmentId = getIntent().getStringExtra("selectedEquipmentId");
                int technicianID = techniciansData.getEmpId();
                Intent intent = new Intent(technicians.this, assign_work.class);
                intent.putExtra("technicianID", technicianID);
                // Pass data to the next activity if needed
                intent.putExtra("selectedEquipmentId", selectedEquipmentId);
                startActivity(intent);
            }
        });
    }

}