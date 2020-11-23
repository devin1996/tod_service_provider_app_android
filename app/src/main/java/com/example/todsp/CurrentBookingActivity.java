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

public class CurrentBookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking);

        Intent intent = getIntent();
        String textval = intent.getStringExtra(ScanConductorActivity.EXTRA_TEXT);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        ListView myListView = (ListView) findViewById(R.id.issuesListView);

        final ArrayList<Map<String, Object>> issuesList = new ArrayList<Map<String, Object>>();

        final SimpleAdapter simpleAdapter = new SimpleAdapter(
                this ,
                issuesList ,
                R.layout.activity_current_booking ,
                new String[]{"date_dri" , "start_date_dri" , "end_date_dri" , "qr_val_dri","passenger_mobile","noseats"} ,
                new int[]{R.id.date_dri , R.id.start_date_dri , R.id.end_date_dri , R.id.qr_val_dri,R.id.passenger_mobile,R.id.noseats}
        );

        myListView.setAdapter(simpleAdapter);

        // let's read the issues from Google Firebase!!
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("bookingList").child("buses").child("AllConfirmedBookings");
        dbRef.orderByChild("rideNo").startAt(textval).endAt(textval + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                issuesList.clear();

                for (DataSnapshot issue : snapshot.getChildren()) {
                    Map<String, Object> issueRow = new HashMap<String, Object>();

                    issueRow.put("date_dri" , "Departure Time : " + issue.child("depTime").getValue(String.class));
                    issueRow.put("start_date_dri" , "Arrival Time : " + issue.child("arrTime").getValue(String.class));
                    issueRow.put("end_date_dri" , "From : " + issue.child("from").getValue(String.class));
                    issueRow.put("qr_val_dri" , "To : " + issue.child("to").getValue(String.class));
                    issueRow.put("passenger_mobile" , "Passenger : " + issue.child("userMobile").getValue(String.class));
                    issueRow.put("noseats" , "Number of Seats : " + issue.child("numberOfSeats").getValue(String.class));

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