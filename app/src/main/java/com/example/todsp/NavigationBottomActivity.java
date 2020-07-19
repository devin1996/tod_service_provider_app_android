package com.example.todsp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationBottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bottom);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dahshboard:
                        startActivity(new Intent(getApplicationContext() , NavBottomActivity2.class));
                        overridePendingTransition(0 , 0);
                        return true;
                    case R.id.home:
                        System.out.println("homie");
                        return false;
                    case R.id.logout:
                        startActivity(new Intent(getApplicationContext() , NavBottomActivity3.class));
                        overridePendingTransition(0 , 0);
                        return true;
                }
                return false;
            }
        });
//                                                                           BottomNavigationView.OnNavigationItemReselectedListener() {
//
//            @Override
//            public vo onNavigationItemReselected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.dahshboard:
//                        startActivity(new Intent(getApplicationContext() , NavBottomActivity2.class));
//                        overridePendingTransition(0 , 0);
//                    case R.id.home:
//                        System.out.println("homie");
//                    case R.id.logout:
//                        startActivity(new Intent(getApplicationContext() , NavBottomActivity3.class));
//                        overridePendingTransition(0 , 0);
//                }
//            }
//        });

    }
}