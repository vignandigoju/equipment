package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class assign_work extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_work);
        String selectedEquipmentId = getIntent().getStringExtra("selectedEquipmentId");
        String filterText = getIntent().getStringExtra("filterText");
        int technicianID = getIntent().getIntExtra("technicianID", 0);
        TextView textView29 = findViewById(R.id.textView29);
        textView29.setText(String.valueOf(technicianID));
        EditText editTextText6 = findViewById(R.id.editTextText6);
        EditText editTextText4 = findViewById(R.id.editTextText4);
        EditText editTextText7 = findViewById(R.id.editTextText7);
        EditText locationInput = findViewById(R.id.locationInput);
        ImageButton backButton = findViewById(R.id.backHome3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to manager_home activity
                Intent intent = new Intent(assign_work.this, technicians.class);
                intent.putExtra("selectedEquipmentId", selectedEquipmentId);
                intent.putExtra("filterText", filterText);
                startActivity(intent);
            }
        });


        // Inside the onClickListener for the "Done" button
        Button doneButton = findViewById(R.id.button14); // Replace with your actual button ID

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from the UI components
                String jobID = editTextText6.getText().toString();
                String serviceType = editTextText4.getText().toString();
                String date = editTextText7.getText().toString();
                String location = locationInput.getText().toString();
                String empIdStr = textView29.getText().toString();
                int empId = Integer.parseInt(empIdStr);

                // Get reference to the jobHistory node
                DatabaseReference jobHistoryRef = FirebaseDatabase.getInstance().getReference("portal").child("jobHistory");

                // Determine the new key
                jobHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long numberOfChildren = snapshot.getChildrenCount();
                        String newKey = String.valueOf(numberOfChildren + 1);

                        // Create a new node under jobHistory with the specified attributes
                        DatabaseReference newJobNode = jobHistoryRef.child(newKey);
                        newJobNode.child("jobID").setValue(jobID);
                        newJobNode.child("serviceType").setValue(serviceType);
                        newJobNode.child("date").setValue(date);
                        newJobNode.child("status").setValue("pending");
                        newJobNode.child("location").setValue(location);
                        newJobNode.child("empId").setValue(empId);
                        newJobNode.child("serviceCharge").setValue(" ");

                        DatabaseReference equipmentJobHistoryRef = FirebaseDatabase.getInstance().getReference("portal").child("Equipment2").child(selectedEquipmentId).child("job_history");
                        equipmentJobHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long numberOfEquipmentChildren = snapshot.getChildrenCount();
                                String newEquipmentKey = "job_history" + String.valueOf(numberOfEquipmentChildren + 1);

                                // Create a new node under the specific equipment's job_history with the specified attributes
                                DatabaseReference newEquipmentJobNode = equipmentJobHistoryRef.child(newEquipmentKey);
                                newEquipmentJobNode.child("date").setValue(date);
                                   newEquipmentJobNode.child("serviceCharge").setValue("pending");
                                newEquipmentJobNode.child("serviceType").setValue(serviceType);
                                newEquipmentJobNode.child("status").setValue("pending");
                                newEquipmentJobNode.child("jobID").setValue(jobID);
                                newEquipmentJobNode.child("empId").setValue(empId);
                                newEquipmentJobNode.child("location").setValue(location);
                                newEquipmentJobNode.child("serviceCharge").setValue(" ");

                        // Navigate to activity_sucAssign
                        Intent intent = new Intent(assign_work.this, workSuccess.class);
                        intent.putExtra("successMessage", "Job ID : " + jobID);
                        startActivity(intent);
                    }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error if needed
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                    }
                });
            }
        });

    }

}