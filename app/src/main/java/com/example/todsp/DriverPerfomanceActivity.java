package com.example.todsp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todsp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverPerfomanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_perfomance);

        ListView myListView = (ListView) findViewById(R.id.issuesListView);

        final ArrayList<Map<String, Object>> issuesList = new ArrayList<Map<String, Object>>();

        final SimpleAdapter simpleAdapter = new SimpleAdapter(
                this ,
                issuesList ,
                R.layout.activity_driver_perfomance ,
                new String[]{"date_dri" , "start_date_dri" , "end_date_dri" , "qr_val_dri"} ,
                new int[]{R.id.date_dri , R.id.start_date_dri , R.id.end_date_dri , R.id.qr_val_dri}
        );

        myListView.setAdapter(simpleAdapter);

        // let's read the issues from Google Firebase!!
        DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference("attendance").child("driver").child(Prevalent.currentOnlineUser.getPhone());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                issuesList.clear();

                for (DataSnapshot issue : snapshot.getChildren()) {
                    Map<String, Object> issueRow = new HashMap<String, Object>();

                    issueRow.put("date_dri" , "Date : " + issue.child("Date").getValue(String.class));
                    issueRow.put("start_date_dri" , "Start Date : " + issue.child("startTime").getValue(String.class));
                    issueRow.put("end_date_dri" , "End Date : " + issue.child("endTime").getValue(String.class));
                    issueRow.put("qr_val_dri" , "QR Code : " + issue.child("qrValue").getValue(String.class));

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