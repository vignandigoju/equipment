package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class manager extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText editTextText, editTextTextPassword;
    private EditText passwordEditText;
    private TextView errorTextView;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        usernameEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        errorTextView = findViewById(R.id.errorTextView);
        usernameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mAuth = FirebaseAuth.getInstance();
        editTextText = findViewById(R.id.editTextText);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);

        // Add TextWatcher to validate email address and update error message
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (passwordEditText.hasFocus()) {
                    validateUsername(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed in this case
            }
        });
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // Validate username when focus moves to the password field
                if (hasFocus) {
                    validateUsername(usernameEditText.getText().toString());
                } else {
                    // Hide the error message when focus is not on the password field
                    errorTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void validateUsername(String username) {
        if (username.length() > 3 && Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            errorTextView.setVisibility(View.GONE);
        } else {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Invalid username");
            errorTextView.setTextColor(Color.RED);
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void togglePasswordVisibility(View view) {
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);

        // Toggle the input type between textPassword and text
        int inputType = passwordEditText.getInputType();
        if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        // Move the cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Revert back to textPassword after 1 second
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        }, 300); // 1000 milliseconds = 1 second
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void onLoginClick(View view) {
        Log.d("Debug", "onLoginClick triggered");

        String email = editTextText.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            showProgressDialog("Please wait while logging in...");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Debug", "onComplete triggered");
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                // Check if the logged-in user is a supervisor
                                checkManagerRole(email);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("Debug", "Authentication failed: " + task.getException());
                                Toast.makeText(manager.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkManagerRole(String email) {
        Log.d("Debug", "Entering checkSupervisorRole");

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("portal").child("users");

        usersReference.orderByChild("username").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Debug", "onDataChange triggered");

                if (dataSnapshot.exists()) {
                    // User with the specified email found
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String role = userSnapshot.child("role").getValue(String.class);
                        if ("manager".equals(role)) {
                            // The user is a supervisor, allow access
                            Toast.makeText(manager.this, "Supervisor Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(manager.this, manager_home.class);
                            startActivity(intent);
                            finish();
                            // Navigate to the next activity or perform necessary actions for supervisors
                        } else {
                            // The user is not a supervisor, show an error message or handle accordingly
                            mAuth.signOut();
                            Toast.makeText(manager.this, "Invalid user role", Toast.LENGTH_SHORT).show();
                        }
                        // Break the loop after processing the first matching user
                        break;
                    }
                } else {
                    // User with the specified email not found
                    mAuth.signOut();
                    Toast.makeText(manager.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.d("Debug", "onCancelled triggered");
                Toast.makeText(manager.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}




