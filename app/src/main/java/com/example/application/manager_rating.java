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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class manager_rating extends AppCompatActivity {
    private int selectedRating = 0;
    private FirebaseDatabase database;
    private DatabaseReference ratingsReference;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_rating);
        ImageView imageView10 = findViewById(R.id.imageView10);
        ImageView imageView11 = findViewById(R.id.imageView11);
        ImageView imageView12 = findViewById(R.id.imageView12);
        ImageView imageView13 = findViewById(R.id.imageView13);
        ImageView imageView14 = findViewById(R.id.imageView14);
        Button addWorkerButton = findViewById(R.id.button11);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        database = FirebaseDatabase.getInstance();
        ratingsReference = database.getReference().child("portal").child("ratings");
        addWorkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get inputs from the user
                EditText jobIDEditText = findViewById(R.id.textView12);
                EditText supervisorEditText = findViewById(R.id.textView16);
                EditText commentEditText = findViewById(R.id.textView20);

                String jobID = jobIDEditText.getText().toString().trim();
                String supervisor = supervisorEditText.getText().toString().trim();
                String comment = commentEditText.getText().toString().trim();

                // Get the selected rating (You may use the selectedRating variable from the previous example)
                int selectedRating = getSelectedRating();

                // Validate inputs
                if (jobID.isEmpty() || supervisor.isEmpty() || comment.isEmpty() || selectedRating == 0) {
                    // Show a toast message if any field is empty or rating is not selected
                    Toast.makeText(manager_rating.this, "All fields must be filled and rating must be selected", Toast.LENGTH_SHORT).show();
                } else {
                    // Now you can add these values to the database
                    addToDatabase(jobID, supervisor, selectedRating, comment);

                    // Optionally, you can clear the input fields or perform other actions
                    jobIDEditText.setText("");
                    supervisorEditText.setText("");
                    commentEditText.setText("");
                    // Other actions...

                    // Show a success message or perform any other UI updates
                    Toast.makeText(manager_rating.this, "Data added to database", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(manager_rating.this, sup_success.class);
                    intent.putExtra("successMessage", "Rating added successfully");
                    startActivity(intent);
                }
            }
        });

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
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(manager_rating.this, manager_home.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_job_history) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_rating.this, manager_job_history.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_rating.this, manager_profile.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_logout){
                    showLogoutConfirmationDialog();
                }else if (item.getItemId() == R.id.nav_rating) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (item.getItemId() == R.id.nav_issues) {
                    // Handle Job History click
                    Intent intent = new Intent(manager_rating.this, manager_issues.class);
                    startActivity(intent);
                }
                // Close the drawer when an item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(1); // Update the rating to 1
                updateStarImages(1); // Update the star images
            }
        });

        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(2);
                updateStarImages(2);
            }
        });

        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(3);
                updateStarImages(3);
            }
        });

        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(4);
                updateStarImages(4);
            }
        });

        imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(5);
                updateStarImages(5);
            }
        });

        // Other initialization code...
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
        Intent intent = new Intent(manager_rating.this, manager.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void updateRating(int rating) {
        selectedRating = rating;
        // Update your database with the selected rating
        // Use your database reference and update the "rating" node with jobid, supervisor, comment, and the selected rating
    }

    private void updateStarImages(int selectedRating) {
        // Update the srcCompat of the ImageViews based on the selectedRating

        ImageView imageView10 = findViewById(R.id.imageView10);
        ImageView imageView11 = findViewById(R.id.imageView11);
        ImageView imageView12 = findViewById(R.id.imageView12);
        ImageView imageView13 = findViewById(R.id.imageView13);
        ImageView imageView14 = findViewById(R.id.imageView14);

        // Reset all stars to empty_star
        imageView10.setImageResource(R.drawable.empty_star);
        imageView11.setImageResource(R.drawable.empty_star);
        imageView12.setImageResource(R.drawable.empty_star);
        imageView13.setImageResource(R.drawable.empty_star);
        imageView14.setImageResource(R.drawable.empty_star);

        // Set the selected stars based on the selectedRating
        switch (selectedRating) {
            case 1:
                imageView10.setImageResource(R.drawable.star);
                break;
            case 2:
                imageView10.setImageResource(R.drawable.star);
                imageView11.setImageResource(R.drawable.star);
                break;
            case 3:
                imageView10.setImageResource(R.drawable.star);
                imageView11.setImageResource(R.drawable.star);
                imageView12.setImageResource(R.drawable.star);
                break;
            case 4:
                imageView10.setImageResource(R.drawable.star);
                imageView11.setImageResource(R.drawable.star);
                imageView12.setImageResource(R.drawable.star);
                imageView13.setImageResource(R.drawable.star);
                break;
            case 5:
                imageView10.setImageResource(R.drawable.star);
                imageView11.setImageResource(R.drawable.star);
                imageView12.setImageResource(R.drawable.star);
                imageView13.setImageResource(R.drawable.star);
                imageView14.setImageResource(R.drawable.star);
                break;
            default:
                // Do nothing
                break;
        }
    }

    private int getSelectedRating() {
        return selectedRating;
    }

    private void addToDatabase(String jobID, String supervisor, int rating, String comment) {
        // Create a new key for the data under "ratings"
        ratingsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long ratingsCount = dataSnapshot.getChildrenCount();

                // Set the key for the new rating as (count + 1)
                String newRatingKey = "rating" + String.valueOf(ratingsCount + 1);

                // Create a new node under "ratings" with the specified key and add the data
                DatabaseReference newRatingReference = ratingsReference.child(newRatingKey);
                newRatingReference.child("jobid").setValue(jobID);
                newRatingReference.child("supervisor").setValue(supervisor);
                newRatingReference.child("rating").setValue(rating);
                newRatingReference.child("comment").setValue(comment);
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}