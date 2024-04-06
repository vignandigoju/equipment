package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class sup_success extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_success);
        Button buttonGoBack = findViewById(R.id.button8);
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to supervisor_home activity
                Intent intent = new Intent(sup_success.this, manager_home.class);
                startActivity(intent);
                finish(); // Optional: Finish the current activity if you don't want to return to it later
            }
        });
        TextView textViewSuccess = findViewById(R.id.textView6);
        String successMessage = getIntent().getStringExtra("successMessage");
        if (successMessage != null) {
            textViewSuccess.setText(successMessage);
        }
    }
    @Override
    public void onBackPressed() {
        // Override the default behavior of the back button
        super.onBackPressed();
        Intent intent = new Intent(sup_success.this, manager_home.class);
        startActivity(intent);
        finish(); // Optional: Finish the current activity if you don't want to return to it later
    }
}