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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class equipment_details extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Myjob_equipAdapter myjobEquipAdapter;
    ArrayList<job_equp_data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_details);
        Button assignWork = findViewById(R.id.button13);
        recyclerView = findViewById(R.id.recyclerViewEquip);
        String selectedEquipmentId = getIntent().getStringExtra("selectedEquipmentId");
        String filterText = getIntent().getStringExtra("filterText");
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("Equipment2").child(selectedEquipmentId).child("job_history");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myjobEquipAdapter = new Myjob_equipAdapter(this,list);
        recyclerView.setAdapter(myjobEquipAdapter);
        ImageButton backButton = findViewById(R.id.backHome2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to manager_home activity
                Intent intent = new Intent(equipment_details.this, equipment.class);
                intent.putExtra("selectedEquipmentId", selectedEquipmentId);
                intent.putExtra("filterText",filterText);
                startActivity(intent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot.getValue());
                    String date = dataSnapshot.child("date").getValue(String.class);
                    String serviceCharge = dataSnapshot.child("serviceCharge").getValue(String.class);
                    String serviceType = dataSnapshot.child("serviceType").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);

                    if (date != null && serviceCharge != null && serviceType != null && status != null) {
                        date = date.trim();
                        serviceCharge = serviceCharge.trim();
                        serviceType = serviceType.trim();
                        status = status.trim();

                        job_equp_data jobEqupData = new job_equp_data(date, serviceCharge, serviceType, status);
                        list.add(jobEqupData);
                        Log.d("AdapterUpdate", "Item added to list: " + jobEqupData.getDate());
                    } else {
                        Log.d("AdapterUpdate", "Skipping item. Values: "
                                + "date=" + date
                                + ", serviceCharge=" + serviceCharge
                                + ", serviceType=" + serviceType
                                + ", status=" + status);
                    }
                }
                Log.d("AdapterUpdate", "Item count before update: " + list.size());
                myjobEquipAdapter.notifyDataSetChanged();
                Log.d("AdapterUpdate", "Item count after update: " + list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (selectedEquipmentId != null) {
            DatabaseReference equipmentReference = FirebaseDatabase.getInstance().getReference("portal").child("Equipment2").child(selectedEquipmentId);

            equipmentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Data for the selected equipment exists in the database
                        // Retrieve the values and populate TextViews

                        String brand = snapshot.child("Brand").getValue(String.class);
                        String type = snapshot.child("Type ").getValue(String.class);
                        String price = snapshot.child("Price").getValue(String.class);
                        String purchaseDate = snapshot.child("Purchase Date").getValue(String.class);
                        String warranty = snapshot.child("Warranty").getValue(String.class);
                        String warrantyExpiry = snapshot.child("Warranty Date End by").getValue(String.class);

                        // Assuming you have TextViews with these IDs in your XML layout
                        TextView equipInput = findViewById(R.id.equipInput);
                        TextView brandInput = findViewById(R.id.brandInput);
                        TextView typeInput = findViewById(R.id.typeInput);
                        TextView priceInput = findViewById(R.id.priceInput);
                        TextView purchasedateInput = findViewById(R.id.purchasedateInput);
                        TextView warrantyInput = findViewById(R.id.warrantyInput);
                        TextView expiryInput = findViewById(R.id.expiryInput);

                        // Set the text of TextViews with the retrieved data
                        equipInput.setText(selectedEquipmentId); // Assuming the ID should be displayed
                        brandInput.setText(brand);
                        typeInput.setText(type);
                        priceInput.setText(price);
                        purchasedateInput.setText(purchaseDate);
                        warrantyInput.setText(warranty);
                        expiryInput.setText(warrantyExpiry);
                    } else {
                        // Data for the selected equipment does not exist
                        // Handle this case based on your requirements
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error if the data retrieval is unsuccessful
                }
            });
        } else {
            // Handle the case where selectedEquipmentId is null
            // You may want to show an error message or finish the activity
            // depending on your requirements
        }


        assignWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to manager_home activity
                Intent intent = new Intent(equipment_details.this, technicians.class);
                intent.putExtra("selectedEquipmentId", selectedEquipmentId);
                intent.putExtra("filterText", filterText);
                startActivity(intent);
            }
        });
    }

}