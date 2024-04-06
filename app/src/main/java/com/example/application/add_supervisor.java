package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.ProgressDialog;
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

public class
 add_supervisor extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supervisor);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Button addSupervisorButton = findViewById(R.id.button5);
        ImageButton backButton = findViewById(R.id.menubutton2);

        addSupervisorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Adding Supervisor...");
                progressDialog.show();

                // Your code to add a new supervisor to the database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                EditText usernameEditText = findViewById(R.id.editTextTextEmailAddress2);
                EditText passwordEditText = findViewById(R.id.editTextTextPassword2);
                EditText nameEditText = findViewById(R.id.editTextText2);

                final String username = usernameEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                final String name = nameEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    // Show a toast message if fields are empty
                    Toast.makeText(add_supervisor.this, "Fields must be filled", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss(); // Dismiss the progress dialog
                } else {
                    Query portalCountQuery = databaseReference.child("portal").child("users");
                    portalCountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long userCount = dataSnapshot.getChildrenCount();

                            // Set the key for the new user as "user" + (count + 1)
                            String newUserKey = "user" + (userCount + 1);

                            // Create a new user with email and password
                            mAuth.createUserWithEmailAndPassword(username, password)
                                    .addOnCompleteListener(add_supervisor.this, new OnCompleteListener<AuthResult>() {
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
                                                    userMap.put("role", "supervisor");
                                                    userMap.put("username", username);
                                                    userMap.put("password", password);

                                                    databaseReference.child("portal").child("users").child(newUserKey).setValue(userMap);
                                                }

                                                // Show a toast message indicating success
                                                Query supervisorsCountQuery = databaseReference.child("portal").child("supervisors");
                                                supervisorsCountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        long supervisorCount = dataSnapshot.getChildrenCount();

                                                        // Set the key for the new supervisor as "supervisor" + (count + 1)
                                                        String newSupervisorKey = "supervisor" + (supervisorCount + 1);

                                                        Map<String, Object> supervisorMap = new HashMap<>();
                                                        supervisorMap.put("username", username);
                                                        supervisorMap.put("name", name);

                                                        databaseReference.child("portal").child("supervisors").child(newSupervisorKey).setValue(supervisorMap);

                                                        Intent intent = new Intent(add_supervisor.this, sup_success.class);
                                                        intent.putExtra("successMessage", "Supervisor added successfully");
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
                                                Toast.makeText(add_supervisor.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
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
                Intent intent = new Intent(add_supervisor.this, manager_home.class);
                startActivity(intent);
            }
        });
    }
}