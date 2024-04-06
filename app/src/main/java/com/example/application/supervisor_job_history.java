package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class supervisor_job_history extends AppCompatActivity {

    Button button10,button9;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_job_history);
        button10 = findViewById(R.id.button10);
        button9 = findViewById(R.id.button9);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.textView4);
        TextView textViewEmail = headerView.findViewById(R.id.textView5);
        textViewUsername.setText("Supervisor");
        textViewEmail.setText(email);
        replaceFragment(new supervisor_pendingJob());
        ImageButton openDrawerButton = findViewById(R.id.menubutton3);
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
                textViewUsername.setText("Supervisor");
                textViewEmail.setText(email);
                Log.d("Debug", "onNav triggered");
                // Handle navigation item clicks
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(supervisor_job_history.this, supervisor_home.class);
                    startActivity(intent);

                } else if (item.getItemId() == R.id.nav_job_history) {
                    // Handle Job History click
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisor_job_history.this, supervisor_profile.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_logout) {
                    showLogoutConfirmationDialog();
                } else if (item.getItemId() == R.id.nav_raiseIssues) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisor_job_history.this, supervisior_raise_issues.class);
                    startActivity(intent);
                }
                // Close the drawer when an item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button10.setTextColor(getResources().getColor(R.color.white));
                button10.setBackgroundResource(R.drawable.section);

                // Reset the color and background for button9
                button9.setTextColor(getResources().getColor(R.color.theme));
                button9.setBackgroundResource(R.drawable.section2);
                replaceFragment(new supervisor_pendingJob());
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button9.setTextColor(getResources().getColor(R.color.white));
                button9.setBackgroundResource(R.drawable.section);

                // Reset the color and background for button9
                button10.setTextColor(getResources().getColor(R.color.theme));
                button10.setBackgroundResource(R.drawable.section2);
                replaceFragment((new supervisorFinJob()));
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
            Intent intent = new Intent(supervisor_job_history.this, manager.class);
            startActivity(intent);
            finish(); // Close the current activity
        }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }
}