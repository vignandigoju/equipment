package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class supervisor_home extends AppCompatActivity {
    DatabaseReference databaseReference;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.textView4);
        TextView textViewEmail = headerView.findViewById(R.id.textView5);
        textViewUsername.setText("Supervisor");
        textViewEmail.setText(email);

        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("category");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GridLayout gridLayout = findViewById(R.id.gridLayout);
                gridLayout.removeAllViews(); // Clear existing views

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String categoryName = dataSnapshot.child("eqName").getValue(String.class);
                    createButton(gridLayout, categoryName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                View headerView = navigationView.getHeaderView(0);
                TextView textViewUsername = headerView.findViewById(R.id.textView4);
                TextView textViewEmail = headerView.findViewById(R.id.textView5);
                textViewUsername.setText("Supervisor");
                textViewEmail.setText(email);
                Log.d("Debug", "onNav triggered");
                // Handle navigation item clicks
                if (item.getItemId() == R.id.nav_home) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (item.getItemId() == R.id.nav_job_history) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisor_home.this, supervisor_job_history.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisor_home.this, supervisor_profile.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_logout){
                    showLogoutConfirmationDialog();
                }else if (item.getItemId() == R.id.nav_raiseIssues) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisor_home.this, supervisior_raise_issues.class);
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
        Intent intent = new Intent(supervisor_home.this, manager.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
    private void createButton(GridLayout gridLayout, String categoryName) {
        Button button = new Button(this);
        button.setText(categoryName);
        button.setLayoutParams(new GridLayout.LayoutParams());

        button.setBackgroundResource(R.drawable.button_design);

        // Set text color
        button.setTextColor(getResources().getColor(R.color.white));
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.setMargins(25, 25, 25, 25); // left, top, right, bottom
        button.setLayoutParams(layoutParams);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = ((Button) v).getText().toString();
                Intent intent = new Intent(supervisor_home.this, equipment.class);
                intent.putExtra("filterText", buttonText);
                startActivity(intent);
            }
        });

        gridLayout.addView(button);
    }
}