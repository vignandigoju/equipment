
package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class supervisor_home2 extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home2);

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
                Intent intent = new Intent(supervisor_home2.this, equipment.class);
                intent.putExtra("filterText", buttonText);
                startActivity(intent);
            }
        });

        gridLayout.addView(button);
    }

    public void onClick(View view) {
        // Handle button click event
    }
}
