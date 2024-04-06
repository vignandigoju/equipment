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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class supervisior_raise_issues extends AppCompatActivity  {

    RecyclerView recyclerView;
    MyIssuesAdapter myIssuesAdapter;
    DatabaseReference databaseReference;
    ArrayList<man_issues_data> list;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisior_raise_issues);
        recyclerView = findViewById(R.id.recyclerView);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("portal").child("Issues");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
//        manIssueAdapter = new manIssueAdapter(this, list);
//        recyclerView.setAdapter(manIssueAdapter);
//        manIssueAdapter.setOnButtonClickListener(this);
        myIssuesAdapter = new MyIssuesAdapter(this, list, true);
        recyclerView.setAdapter(myIssuesAdapter);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageButton openDrawerButton = findViewById(R.id.menubutton4);
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.textView4);
        TextView textViewEmail = headerView.findViewById(R.id.textView5);
        textViewUsername.setText("Supervisor");
        textViewEmail.setText(email);
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
                if (item.getItemId() == R.id.nav_raiseIssues) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (item.getItemId() == R.id.nav_job_history) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisior_raise_issues.this, supervisor_job_history.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisior_raise_issues.this, supervisor_profile.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_logout){
                    showLogoutConfirmationDialog();
                }else if (item.getItemId() == R.id.nav_home) {
                    // Handle Job History click
                    Intent intent = new Intent(supervisior_raise_issues.this, supervisor_home.class);
                    startActivity(intent);
                }
                // Close the drawer when an item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    man_issues_data manIssuesData = dataSnapshot.getValue(man_issues_data.class);
                    list.add(manIssuesData);
                }
                myIssuesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "onCancelled: " + error.getMessage());
            }
        });
    }

//    @Override
//    public void onButtonClick(int position) {
//        // Get the specific equipment ID from the clicked item
//        String equipmentId = list.get(position).getEquipmentId(); // Assuming getEquipmentId() returns the equipment ID
//        Log.d("ButtonClick", "Equipment ID: " + equipmentId);
//
//        // Find the issue node with matching equipmentId
//        DatabaseReference issuesRef = databaseReference;
//        issuesRef.orderByChild("equipmentId").equalTo(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    // Get the key of the matched issue node
//                    String issueKey = snapshot.getKey();
//                    Log.d("ButtonClick", "Issue Key: " + issueKey);
//
//                    // Update the status under the matched issue node
//                    DatabaseReference issueRef = issuesRef.child(issueKey).child("status");
//                    issueRef.setValue("completed").addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d("FirebaseUpdate", "Status updated successfully for issue with equipment ID: " + equipmentId);
//                            } else {
//                                Log.e("FirebaseUpdate", "Failed to update status for issue with equipment ID: " + equipmentId);
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("FirebaseError", "onCancelled: " + databaseError.getMessage());
//            }
//        });
//    }
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
        Intent intent = new Intent(supervisior_raise_issues.this, supervisor.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}
