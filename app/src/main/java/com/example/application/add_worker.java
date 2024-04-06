package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class add_worker extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Worker...");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Button addWorkerButton = findViewById(R.id.button5);
        ImageButton backButton = findViewById(R.id.menubutton2);

        addWorkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText workerNameEditText = findViewById(R.id.editTextText2);
                EditText employeeIdEditText = findViewById(R.id.editTextTextEmailAddress2);
                EditText usernameEditText = findViewById(R.id.editTextTextEmailAddress4);
                EditText passwordEditText = findViewById(R.id.editTextTextPassword3);

                final String workerName = workerNameEditText.getText().toString();
                final String employeeIdStr = employeeIdEditText.getText().toString();
                final String username = usernameEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

//                final String workerName = workerNameEditText.getText().toString();
//                final String employeeId = employeeIdEditText.getText().toString();

                if (workerName.isEmpty() || employeeIdStr.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    // Show a toast message if fields are empty
                    Toast.makeText(add_worker.this, "Fields must be filled", Toast.LENGTH_SHORT).show();
                } else {

                    int employeeId = Integer.parseInt(employeeIdStr);
                    showProgressDialog();

                    // Set the key for the new worker as "worker" + (count + 1)
//                    Query workerCountQuery = databaseReference.child("portal").child("workers");
//                    workerCountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            long workerCount = dataSnapshot.getChildrenCount();
//                            String newWorkerKey = "worker" + (workerCount + 1);
//
//                            Map<String, Object> workerMap = new HashMap<>();
//                            workerMap.put("name", workerName);
//                            workerMap.put("empId", employeeId);
//
//                            databaseReference.child("portal").child("workers").child(newWorkerKey).setValue(workerMap);
//
//                            hideProgressDialog();
//                            Intent intent = new Intent(add_worker.this, sup_success.class);
//                            intent.putExtra("successMessage", "Worker added successfully");
//                            startActivity(intent);
                    Query portalCountQuery = databaseReference.child("portal").child("users");
                    portalCountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long userCount = dataSnapshot.getChildrenCount();

                            // Set the key for the new user as "user" + (count + 1)
                            String newUserKey = "user" + (userCount + 1);

                            // Create a new user with email and password
                            mAuth.createUserWithEmailAndPassword(username, password)
                                    .addOnCompleteListener(add_worker.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("TAG", "createUserWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                // Your code to add a new user to the database
                                                if (user != null) {
                                                    Map<String, Object> userMap = new HashMap<>();
                                                    userMap.put("role", "worker");
                                                    userMap.put("username", username);
                                                    userMap.put("password", password);

                                                    databaseReference.child("portal").child("users").child(newUserKey).setValue(userMap);
                                                }

                                                // Show a toast message indicating success
                                                Query supervisorsCountQuery = databaseReference.child("portal").child("workers");
                                                supervisorsCountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        long supervisorCount = dataSnapshot.getChildrenCount();

                                                        // Set the key for the new supervisor as "supervisor" + (count + 1)
                                                        String newSupervisorKey = "worker" + (supervisorCount + 1);

                                                        Map<String, Object> supervisorMap = new HashMap<>();
                                                        supervisorMap.put("username", username);
                                                        supervisorMap.put("empId", employeeId);
                                                        supervisorMap.put("name", workerName);

                                                        databaseReference.child("portal").child("workers").child(newSupervisorKey).setValue(supervisorMap);

                                                        Intent intent = new Intent(add_worker.this, sup_success.class);
                                                        intent.putExtra("successMessage", "Worker added successfully");
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // Handle database error
                                                        Log.e("TAG", "Database error: " + databaseError.getMessage());
                                                    }
                                                });
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(add_worker.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressDialog();
                            Log.e("TAG", "Database error: " + databaseError.getMessage());
                        }
                    });
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to manager_home activity
                Intent intent = new Intent(add_worker.this, manager_home.class);
                startActivity(intent);
            }
        });
    }

    private void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}