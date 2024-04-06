package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class workSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_success);
        TextView jobID = findViewById(R.id.textView31);
        String successMessage = getIntent().getStringExtra("successMessage");
        Button buttonGoBack = findViewById(R.id.button8);
        if (successMessage != null) {
            jobID.setText(successMessage);
        }
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to supervisor_home activity
                Intent intent = new Intent(workSuccess.this, supervisor_home.class);
                startActivity(intent);
                finish(); // Optional: Finish the current activity if you don't want to return to it later
            }
        });
    }
}