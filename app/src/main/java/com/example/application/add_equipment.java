package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class add_equipment extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);
        ImageButton backButton = findViewById(R.id.menubutton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to manager_home activity
                Intent intent = new Intent(add_equipment.this, manager_home.class);
                startActivity(intent);
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText categoryEditText = findViewById(R.id.editTextText2);
        EditText equipmentIdEditText = findViewById(R.id.editTextTextEmailAddress2);
        EditText brandEditText = findViewById(R.id.editTextTextPassword2);
        EditText priceEditText = findViewById(R.id.editTextText3);
        EditText purchaseDateEditText = findViewById(R.id.editTextTextEmailAddress);
        EditText typeEditText = findViewById(R.id.catergory);
        EditText warrantyEditText = findViewById(R.id.editTextTextEmailAddress3);
        EditText warrantyEndDateEditText = findViewById(R.id.editTextTextPassword4);
        Button addButton = findViewById(R.id.button5);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = categoryEditText.getText().toString().trim();
                String equipmentId = equipmentIdEditText.getText().toString().trim();
                String brand = brandEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                String purchaseDate = purchaseDateEditText.getText().toString().trim();
                String type = typeEditText.getText().toString().trim();
                String warranty = warrantyEditText.getText().toString().trim();
                String warrantyEndDate = warrantyEndDateEditText.getText().toString().trim();

                saveDataToFirebase(category, equipmentId, brand, price, purchaseDate, type, warranty, warrantyEndDate);
            }
        });
    }

    private void saveDataToFirebase(String category, String equipmentId, String brand, String price, String purchaseDate, String type, String warranty, String warrantyEndDate) {
        DatabaseReference equipmentRef = mDatabase.child("portal").child("Equipment");
        equipmentRef.child(equipmentId).setValue(category);

        DatabaseReference equipment2Ref = mDatabase.child("portal").child("Equipment2").child(equipmentId);

        Map<String, Object> equipmentDetails = new HashMap<>();
        equipmentDetails.put("Brand", brand);
        equipmentDetails.put("Equipment", category);
        equipmentDetails.put("Price", price);
        equipmentDetails.put("Purchase Date", purchaseDate);
        equipmentDetails.put("Type", type);
        equipmentDetails.put("Warranty", warranty);
        equipmentDetails.put("Warranty Date End by", warrantyEndDate);

        equipment2Ref.setValue(equipmentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(add_equipment.this, sup_success.class);
                    intent.putExtra("successMessage", "Equipment added successfully");
                    startActivity(intent);
                } else {
                    Toast.makeText(add_equipment.this, "Failed to add equipment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Check if the category exists under portal/category node
        DatabaseReference categoryRef = mDatabase.child("portal").child("category");
        categoryRef.orderByChild("eqName").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // If the category doesn't exist, create a new one
                    categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int numCategories = (int) dataSnapshot.getChildrenCount();
                            String newCategoryKey = "category" + (numCategories + 1);

                            DatabaseReference newCategoryRef = categoryRef.child(newCategoryKey);
                            newCategoryRef.child("eqName").setValue(category);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(add_equipment.this, "Error counting categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(add_equipment.this, "Error checking category: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
