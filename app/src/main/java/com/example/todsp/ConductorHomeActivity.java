package com.example.todsp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConductorHomeActivity extends AppCompatActivity {

    private Button support, attendance, perfomanceAssestment, settings, logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_home);

        support = (Button) findViewById(R.id.btn_con_help1212);
        attendance = (Button) findViewById(R.id.btn_con_attendance1212);
        perfomanceAssestment = (Button) findViewById(R.id.btn_con_perfomance1212);
        settings = (Button) findViewById(R.id.btn_con_settings1212);
        logOut = (Button) findViewById(R.id.btn_con_logout1212);

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(ConductorHomeActivity.this, ScanConductorActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });

        perfomanceAssestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(ConductorHomeActivity.this, ConductorPerfomanceActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(ConductorHomeActivity.this, MainActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(ConductorHomeActivity.this, SettingsConductorActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });

    }
}