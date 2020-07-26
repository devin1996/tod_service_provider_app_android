package com.example.todsp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todsp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ScanDriverActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.example.ptms.EXTRA_TEXT";
    public final static int QRcodeWidth = 350;
    TextView tv_qr_readTxt;
    Button scanQrBtn;
    Button startDayBtn;
    Button stopDayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_driver);

        tv_qr_readTxt = (TextView) findViewById(R.id.dri_qr_read);
        scanQrBtn = (Button) findViewById(R.id.scan_dri_qr);
        startDayBtn = (Button) findViewById(R.id.dri_day_start);
        stopDayBtn = (Button) findViewById(R.id.dri_day_stop);

        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(ScanDriverActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        startDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busDetail = tv_qr_readTxt.getText().toString();
                if (TextUtils.isEmpty(busDetail)) {
                    Toast.makeText(ScanDriverActivity.this , "Please scan the qr code first" , Toast.LENGTH_SHORT).show();
                } else {
                    startDay();
                    Intent intent = new Intent(ScanDriverActivity.this , DriverHomeActivity.class);
                    intent.putExtra(EXTRA_TEXT , busDetail);
                    startActivity(intent);
                }

            }
        });

        stopDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busDetail = tv_qr_readTxt.getText().toString();
                if (TextUtils.isEmpty(busDetail)) {
                    Toast.makeText(ScanDriverActivity.this , "Please scan the qr code first" , Toast.LENGTH_SHORT).show();
                } else {
                    endDay();
                    Intent intent = new Intent(ScanDriverActivity.this , DriverHomeActivity.class);
                    intent.putExtra(EXTRA_TEXT , busDetail);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode , resultCode , data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******" , "Cancelled scan");

            } else {
                Log.e("Scan" , "Scanned");

                tv_qr_readTxt.setText(result.getContents());
                Toast.makeText(this , "Scanned: " + result.getContents() , Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode , resultCode , data);
        }
    }

    public void pushValue() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("QR");

        myRef.setValue(tv_qr_readTxt.getText().toString());
        //myRef.setValue("Scanning My Qr code");
        //travelPlanMap.put("from", fromCity.getText().toString());
    }

    public void startDay() {
        final String startTime, startDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        startDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        startTime = currentTime.format(calForDate.getTime());

//        String userPhone = Prevelent.currentOnlineUser.getPhone();
        //final String reportKey = startDate + Prevalent.currentOnlineUser.getPhone() + QrValue;

        final DatabaseReference reportListRef;
        reportListRef = FirebaseDatabase.getInstance().getReference().child("attendance").child("driver");


        reportListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child(Prevalent.currentOnlineUser.getPhone()).child(startDate).exists())) {
                    HashMap<String, Object> reportMap = new HashMap<>();
                    reportMap.put("qrValue" , tv_qr_readTxt.getText().toString());
                    reportMap.put("Date" , startDate);
                    reportMap.put("startTime" , startTime);

                    reportListRef.child(Prevalent.currentOnlineUser.getPhone()).child(startDate).updateChildren(reportMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(ScanDriverActivity.this , "Your Attendance marked" , Toast.LENGTH_SHORT).show();
                                        //loadingBar.dismiss();

                                        Intent intent = new Intent(ScanDriverActivity.this , DriverHomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        //loadingBar.dismiss();
                                        Toast.makeText(ScanDriverActivity.this , "Network Error.. please try again after some time..." , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ScanDriverActivity.this , "Your attendance already exists" , Toast.LENGTH_SHORT).show();
                    //loadingBar.dismiss();
                    Toast.makeText(ScanDriverActivity.this , "Please try again using another way" , Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ScanDriverActivity.this , DriverHomeActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void endDay( ){

        final String endTime, endDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        endDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        endTime = currentTime.format(calForDate.getTime());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("attendance").child("driver");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("endTime", endTime);
        ref.child(Prevalent.currentOnlineUser.getPhone()).child(endDate).updateChildren(userMap);


        startActivity(new Intent(ScanDriverActivity.this, DriverHomeActivity.class));
        Toast.makeText(ScanDriverActivity.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}