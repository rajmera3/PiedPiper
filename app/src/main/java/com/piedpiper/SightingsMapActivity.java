package com.piedpiper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.piedpiper.R.id.map;

/**
 * Created by pbokey on 10/9/17.
 */

public class SightingsMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    Date start, end;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightingsmap);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        // set default start and end
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR,2017);
        start = cal.getTime();
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.YEAR,2017);
        end = cal.getTime();


        Button setStartButton = (Button) findViewById(R.id.mapstartdate_button_id);
        Button setEndButton = (Button) findViewById(R.id.mapenddate_button_id);

        setStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // new DatePickerDialog
            }
        });
        setEndButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // new DatePickerDialog
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<RatSighting> sightings = MainActivity.sightingsList;
        int count = 0;
        for (RatSighting sighting : sightings) {
            Date date;
            try {
                date = new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a").parse(sighting.getCreatedDate());
            } catch (ParseException e) {
                date = new Date();
            }
            //date.compareTo(start) >= 0 && date.compareTo(end) <= 0
            if (count++ < 10) {
                // between start and end
                LatLng location = new LatLng(Double.parseDouble(sighting.getLatitude()), Double.parseDouble(sighting.getLongitude()));
                googleMap.addMarker(new MarkerOptions().position(location)
                        .title(sighting.getUniqueKey()));
            }
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7128, -74.0060), 11.0f));
    }
}
