package com.piedpiper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sy024598 on 10/17/17.
 * Activity to handle creating a new rat sighting
 */

public class NewRatSightingActivity extends AppCompatActivity{

    private EditText city;
    private EditText borough;
    private EditText latitude;
    private EditText longitude;
    private EditText incidentAddress;
    private EditText incidentZip;
    private EditText locationType;
    private TextView createDate;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newratsighting);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        city = findViewById(R.id.city);

        borough = findViewById(R.id.borough);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        int i = 0;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    i);
        }
        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            i = 1;
        }

        if (i == 1) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location == null) {
                                return;
                            }
                            latitude.setText(Double.toString(location.getLatitude()));
                            longitude.setText(Double.toString(location.getLatitude()));
                        }
                    });
        }
        incidentAddress = findViewById(R.id.incidentaddress);

        incidentZip = findViewById(R.id.incidentzip);

        locationType = findViewById(R.id.locationtype);

        createDate = findViewById(R.id.createdate);
        createDate.setText(android.text.format.DateFormat.format("MM/dd/yy HH:mm",
                new java.util.Date()));

//        TextView uniqueID = findViewById(R.id.uniqueID);
//        uniqueID.setText(uniqueKey);


        Button add = findViewById(R.id.addratsighting_button_id);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatSighting newSighting = new RatSighting(createDate.getText().toString(),
                        locationType.getText().toString(), incidentZip.getText().toString(),
                        incidentAddress.getText().toString(), city.getText().toString(),
                        borough.getText().toString(), latitude.getText().toString(),
                        longitude.getText().toString());
                DatabaseReference dataRef =
                        FirebaseDatabase.getInstance().getReference("sightings");
                DatabaseReference newRef = dataRef.push();
                newSighting.setUniqueKey(newRef.getKey());
                String key = newRef.getKey();
                newRef.setValue(newSighting.getMap());

                if (dataRef.child(key) != null) {
                    Log.d("rahul", dataRef.child(key) + "is here");
                }
                Intent splash = new Intent(getBaseContext(), MainActivity.class);
                startActivity(splash);
            }
        });

        Button cancel = findViewById(R.id.cancelratsighting_button_id);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splash = new Intent(getBaseContext(), MainActivity.class);
                startActivity(splash);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();

    }
}
