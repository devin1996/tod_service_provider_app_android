package com.example.todsp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.todsp.Prevalent.Prevalent;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriversMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    Button LogoutDriBtn, settingsDriBtn;

    private String currentUser;
    private Boolean currentLogoutDriverStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_map);

        currentUser = Prevalent.currentOnlineUser.getPhone();


        LogoutDriBtn = (Button) findViewById(R.id.m_call);
        settingsDriBtn = (Button) findViewById(R.id.m_call2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LogoutDriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLogoutDriverStatus = true;
                dissconnectTheDriver();
                exitTaskDriver();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(mLocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient , mLocationRequest , this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        String driverId = Prevalent.currentOnlineUser.getPhone();

        DatabaseReference DriverAvailabilityref = FirebaseDatabase.getInstance().getReference().child("driversAvailable");

        GeoFire geoFireAvailable = new GeoFire(DriverAvailabilityref);
        geoFireAvailable.setLocation(driverId , new GeoLocation(location.getLatitude() , location.getLongitude()));

//        DatabaseReference DriverWorkingref = FirebaseDatabase.getInstance().getReference().child("drivers_working");
//        GeoFire geoFireWorking = new GeoFire(DriverWorkingref);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        String driverId = Prevalent.currentOnlineUser.getPhone();
//
//        DatabaseReference DriverAvailabilityref = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
//
//        GeoFire geoFireAvailable = new GeoFire(DriverAvailabilityref);
//        geoFireAvailable.removeLocation(driverId);
        if (!currentLogoutDriverStatus) {
            dissconnectTheDriver();
        }

    }

    private void dissconnectTheDriver() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        String driverId = Prevalent.currentOnlineUser.getPhone();
        DatabaseReference DriverAvailabilityref = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
        GeoFire geoFireAvailable = new GeoFire(DriverAvailabilityref);
        geoFireAvailable.removeLocation(driverId);
    }

    private void exitTaskDriver() {
        Intent logOutIntent = new Intent(DriversMapActivity.this , DriverHomeActivity.class);
        logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logOutIntent);
        finish();
    }

}