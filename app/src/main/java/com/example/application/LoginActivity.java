package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    public void supervisor(View view){
        Intent intent = new Intent(this, supervisor.class);
        startActivity(intent);
    }
    public void manager(View view){
        Intent intent = new Intent(this, manager.class);
        startActivity(intent);
    }public void worker(View view){
        Intent intent = new Intent(this, worker.class);
        startActivity(intent);
    }

    public void user(View view){
        Intent intent = new Intent(this, user_login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
}