package com.example.todsp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.paperdb.Paper;

public class ConductorMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Boolean currentLogOutPasStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view , "Replace with your own action" , Snackbar.LENGTH_LONG)
                        .setAction("Action" , null).show();
            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home , R.id.nav_gallery , R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this , R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this , navController , mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView , navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext() , "nav_slideshow is Selected" , Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_gallery:
                        Intent timeScheduleIntent = new Intent(ConductorMainActivity.this , ResetPasswordActivity.class);
                        startActivity(timeScheduleIntent);
                        break;
                    case R.id.nav_slideshow:
                        currentLogOutPasStatus = true;
                        Paper.book().destroy();

                        Intent logoutIntent = new Intent(ConductorMainActivity.this , MainActivity.class);
                        //logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logoutIntent);
                        break;
                }
                drawer.closeDrawers();
                return false;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.conductor_main , menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this , R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController , mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}