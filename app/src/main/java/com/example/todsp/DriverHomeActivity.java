package com.example.todsp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DriverHomeActivity extends AppCompatActivity {

    private Button pickUpRequests, attendance, perfomanceAssestment, settings, logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        pickUpRequests = (Button) findViewById(R.id.btn_dri_pickup1212);
        attendance = (Button) findViewById(R.id.btn_dri_attendance1212);
        perfomanceAssestment = (Button) findViewById(R.id.btn_perfomance1212);
        settings = (Button) findViewById(R.id.btn_dri_settings1212);
        logOut = (Button) findViewById(R.id.btn_dri_logout1212);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(DriverHomeActivity.this, MainActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(DriverHomeActivity.this, ScanDriverActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(DriverHomeActivity.this, SettingsDriverActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });

        pickUpRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(DriverHomeActivity.this, DriverMapsActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });
    }
}