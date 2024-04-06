package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class manager_home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Button addWorkerButton = findViewById(R.id.button7);
        NavigationView navigationView = findViewById(R.id.nav_view);
        addWorkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity (ActivityAddWorker)
                Intent intent = new Intent(manager_home.this, add_worker.class);

                // Optionally, you can pass data to the new activity using intent.putExtra()
                // intent.putExtra("key", "value");

                // Start the new activity
                startActivity(intent);
            }
        });

        ImageButton openDrawerButton = findViewById(R.id.menubutton);
        openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", "Drawer Opened");
                // Open the drawer when the button is clicked
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("Debug", "onNav triggered");
                // Handle navigation item clicks
                if (item.getItemId() == R.id.nav_home) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (item.getItemId() == R.id.nav_job_history) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_home.this, manager_job_history.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_home.this, manager_profile.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_logout){
                    showLogoutConfirmationDialog();
                }else if (item.getItemId() == R.id.nav_rating) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_home.this, manager_rating.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_issues) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_home.this, manager_issues.class);
                    startActivity(intent);
                }
                // Close the drawer when an item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


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
        Intent intent = new Intent(manager_home.this, manager.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    public void onClickLogin(View view) {
        // Create an Intent to start the new activity (ActivityAddSupervisor)
        Intent intent = new Intent(this, add_supervisor.class);

        // Optionally, you can pass data to the new activity using intent.putExtra()
        // intent.putExtra("key", "value");

        // Start the new activity
        startActivity(intent);
    }
    public void addEquipment(View view) {
        // Create an Intent to start the new activity (ActivityAddSupervisor)
        Intent intent = new Intent(this, add_equipment.class);

        // Optionally, you can pass data to the new activity using intent.putExtra()
        // intent.putExtra("key", "value");

        // Start the new activity
        startActivity(intent);
    }public void manageUsers(View view) {
        // Create an Intent to start the new activity (ActivityAddSupervisor)
        Intent intent = new Intent(this, manage_users.class);

        // Optionally, you can pass data to the new activity using intent.putExtra()
        // intent.putExtra("key", "value");

        // Start the new activity
        startActivity(intent);
    }
}