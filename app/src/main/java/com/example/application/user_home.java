package com.example.application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class user_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Button addWorkerButton = findViewById(R.id.button7);
        addWorkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity (ActivityAddWorker)
                Intent intent = new Intent(user_home.this, user_issues.class);

                // Optionally, you can pass data to the new activity using intent.putExtra()
                // intent.putExtra("key", "value");

                // Start the new activity
                startActivity(intent);
            }
        });
    }
    public void onClickLogin(View view) {
        // Create an Intent to start the new activity (ActivityAddSupervisor)
        Intent intent = new Intent(this, user_raise_issue.class);

        // Optionally, you can pass data to the new activity using intent.putExtra()
        // intent.putExtra("key", "value");

        // Start the new activity
        startActivity(intent);
    }
    public void logout(View view){
        showLogoutConfirmationDialog();
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Yes, perform logout
                performLogout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked No, close the dialog
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performLogout() {
        // Log out from Firebase authentication
        FirebaseAuth.getInstance().signOut();

        // Navigate to the login page or any other desired activity
        Intent intent = new Intent(user_home.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}