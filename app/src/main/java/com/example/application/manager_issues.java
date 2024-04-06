package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class manager_issues extends AppCompatActivity {
    RecyclerView recyclerView;
    MyIssuesAdapter myIssuesAdapter;
    DatabaseReference databaseReference;
    ArrayList<man_issues_data> list;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_issues);

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("Issues");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        list = new ArrayList<>();
        myIssuesAdapter = new MyIssuesAdapter(this, list, true);
        recyclerView.setAdapter(myIssuesAdapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    man_issues_data manIssuesData = dataSnapshot.getValue(man_issues_data.class);
                    list.add(manIssuesData);
                }
                myIssuesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

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
                Log.d("Debug", "onNav triggered");
                // Handle navigation item clicks
                if (item.getItemId() == R.id.nav_home) {// Handle Home click
                    Intent intent = new Intent(manager_issues.this, manager_home.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_job_history) {
                    Intent intent = new Intent(manager_issues.this, manager_job_history.class);
                    startActivity(intent);
                    // Handle Job History click

                }else if (item.getItemId() == R.id.nav_issues) {
                    // Handle Job History click
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (item.getItemId() == R.id.nav_logout){
                    showLogoutConfirmationDialog();
                }else if (item.getItemId() == R.id.nav_rating) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_issues.this, manager_rating.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_issues.this, manager_profile.class);
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
        Intent intent = new Intent(manager_issues.this, manager.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}