package com.example.todsp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.todsp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Intent intent = getIntent();
        String textval = intent.getStringExtra(ScanConductorActivity.EXTRA_TEXT);

        ListView myListView = (ListView) findViewById(R.id.issuesListView);

        final ArrayList<Map<String, Object>> issuesList = new ArrayList<Map<String, Object>>();

        final SimpleAdapter simpleAdapter = new SimpleAdapter(
                this ,
                issuesList ,
                R.layout.activity_support ,
                new String[]{"date_dri" , "start_date_dri" , "end_date_dri"} ,
                new int[]{R.id.date_dri , R.id.start_date_dri , R.id.end_date_dri}
        );

        myListView.setAdapter(simpleAdapter);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());
        // let's read the issues from Google Firebase!!
        DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference("askForHelp").child(textval).child(saveCurrentDate);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                issuesList.clear();

                for (DataSnapshot issue : snapshot.getChildren()) {
                    Map<String, Object> issueRow = new HashMap<String, Object>();

                    issueRow.put("date_dri" , "Asked Help : " + issue.child("reviewIssue").getValue(String.class));
                    issueRow.put("start_date_dri" , "Time : " + issue.child("reviewTime").getValue(String.class));
                    issueRow.put("end_date_dri" , "Passenger : " + issue.child("userPhone").getValue(String.class));

                    issuesList.add(issueRow);
                }

                simpleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase" , "Failed to read from Firebase." , error.toException());
            }
        });

    }
}