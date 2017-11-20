package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sy024598 on 10/17/17.
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newratsighting);

        city = findViewById(R.id.city);

        borough = findViewById(R.id.borough);

        latitude = findViewById(R.id.latitude);

        longitude = findViewById(R.id.longitude);

        incidentAddress = findViewById(R.id.incidentaddress);

        incidentZip = findViewById(R.id.incidentzip);

        locationType = findViewById(R.id.locationtype);

        createDate = findViewById(R.id.createdate);
        createDate.setText(android.text.format.DateFormat.format("MM/dd/yy HH:mm", new java.util.Date()));

//        TextView uniqueID = findViewById(R.id.uniqueID);
//        uniqueID.setText(uniqueKey);


        Button add = findViewById(R.id.addratsighting_button_id);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatSighting newSighting = new RatSighting(createDate.getText().toString(), locationType.getText().toString(), incidentZip.getText().toString(), incidentAddress.getText().toString(), city.getText().toString(), borough.getText().toString(), latitude.getText().toString(), longitude.getText().toString());
                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("sightings");
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
