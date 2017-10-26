package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by pbokey on 10/9/17.
 */

public class SightingDetailView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightingdetailview);

        RatSighting sighting = (RatSighting) getIntent().getSerializableExtra("Sighting");

       TextView city = (TextView) findViewById(R.id.city);
        city.setText(sighting.getCity());

        TextView borough = (TextView) findViewById(R.id.borough);
        borough.setText(sighting.getBorough());

        TextView latitude = (TextView) findViewById(R.id.latitude);
        latitude.setText(sighting.getLatitude());

        TextView longitude = (TextView) findViewById(R.id.longitude);
        longitude.setText(sighting.getLongitude());

        TextView incidentAddress = (TextView) findViewById(R.id.incidentaddress);
        incidentAddress.setText(sighting.getIncidentAddress());

        TextView incidentZip = (TextView) findViewById(R.id.incidentzip);
        incidentZip.setText(sighting.getIncidentZip());

        TextView locationType = (TextView) findViewById(R.id.locationtype);
        locationType.setText(sighting.getLocationType());

        TextView createDate = (TextView) findViewById(R.id.createdate);
        createDate.setText(sighting.getCreatedDate());

        TextView uniqueID = (TextView) findViewById(R.id.uniqueID);
        uniqueID.setText(sighting.getUniqueKey());

    }

    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();

    }
}
