package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_raise_issue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_raise_issue);
        EditText textView12 = findViewById(R.id.textView12);
        EditText textView16 = findViewById(R.id.textView16);
        EditText textView14 = findViewById(R.id.textView14);
        EditText textView20 = findViewById(R.id.textView20);
        EditText textView18 = findViewById(R.id.textView18);
        Button doneButton = findViewById(R.id.button15);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from the UI components
                String equipmentId = textView12.getText().toString();
                String equipmentName = textView16.getText().toString();
                String issueType = textView14.getText().toString();
                String comment = textView20.getText().toString();
                String location = textView18.getText().toString();

                if (equipmentId.isEmpty() || equipmentName.isEmpty() || issueType.isEmpty() || comment.isEmpty() || location.isEmpty()) {
                    // Display a message to the user indicating that all fields are required
                    Toast.makeText(user_raise_issue.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return; // Exit the method without proceeding further
                }

                // Get reference to the jobHistory node
                DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference("portal").child("Issues");

                // Determine the new key
                issueRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long numberOfEquipmentChildren = snapshot.getChildrenCount();
                        String newEquipmentKey = "Issue" + String.valueOf(numberOfEquipmentChildren + 1);

                        // Create a new node under jobHistory with the specified attributes
                        DatabaseReference newJobNode = issueRef.child(newEquipmentKey);
                        newJobNode.child("equipmentId").setValue(equipmentId);
                        newJobNode.child("equipmentName").setValue(equipmentName);
                        newJobNode.child("issueType").setValue(issueType);
                        newJobNode.child("comment").setValue(comment);
                        newJobNode.child("location").setValue(location);
                        newJobNode.child("status").setValue("pending");

                        Intent intent = new Intent(user_raise_issue.this, issueSuccessful.class);
                        startActivity(intent);
                        finish();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
}