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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class supervisor_profile extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_profile);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.textView4);
        TextView textViewEmail = headerView.findViewById(R.id.textView5);
        textViewUsername.setText("Supervisor");
//        textViewEmail.setText(email);
        ImageButton openDrawerButton = findViewById(R.id.menubutton4);
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
                View headerView = navigationView.getHeaderView(0);
                TextView textViewUsername = headerView.findViewById(R.id.textView4);
                TextView textViewEmail = headerView.findViewById(R.id.textView5);
                textViewUsername.setText("Supervisor");
//                textViewEmail.setText(email);
                Log.d("Debug", "onNav triggered");
                // Handle navigation item clicks
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(supervisor_profile.this, supervisor_home.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_job_history) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisor_profile.this, supervisor_job_history.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Job History click
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.nav_logout) {
                    showLogoutConfirmationDialog();
                } else if (item.getItemId() == R.id.nav_raiseIssues) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisor_profile.this, supervisior_raise_issues.class);
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
        Intent intent = new Intent(supervisor_profile.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}