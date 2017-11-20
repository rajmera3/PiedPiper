package com.piedpiper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static com.piedpiper.R.id.map;

/**
 * Created by pbokey on 10/9/17.
 * Activity to handle map view of rat sightings
 */

public class SightingsMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int offset = 1900;
    private Date start = new Date(2015 - offset, 1, 1);
    private Date end = new Date(2017 - offset, 1, 1);


    //private DatePickerDialog datePickerDialog;
    private GoogleMap googleMap;
    private static final List<RatSighting> sightingsList = new LinkedList<>();

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




        Button setStartButton = findViewById(R.id.mapstartdate_button_id);
        Button setEndButton = findViewById(R.id.mapenddate_button_id);

        setStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SightingsMapActivity.this,
                        new mDateSetListener(0), start.getYear() + offset, start.getMonth(),
                        start.getDate());
                dialog.show();
            }
        });
        setEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SightingsMapActivity.this,
                        new mDateSetListener(1), end.getYear() + offset, end.getMonth(),
                        end.getDate());
                dialog.show();
            }
        });
    }

    private void updateList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("sightings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot sightingSnapshot: dataSnapshot.getChildren()) {
                    String city = (String) sightingSnapshot.child("City").getValue();
                    String borough = (String) sightingSnapshot.child("Borough").getValue();
                    String incidentAddress =
                            (String) sightingSnapshot.child("Incident Address").getValue();
                    String incidentZip = (String) sightingSnapshot.child("Incident Zip").getValue();
                    String createdDate = (String) sightingSnapshot.child("Created Date").getValue();
                    String locationType =
                            (String) sightingSnapshot.child("Location Type").getValue();
                    String latitude = (String) sightingSnapshot.child("Latitude").getValue();
                    String longitude = (String) sightingSnapshot.child("Longitude").getValue();
                    RatSighting add =
                            new RatSighting(createdDate, locationType, incidentZip,
                                    incidentAddress, city, borough, latitude, longitude);
                    add.setUniqueKey(sightingSnapshot.getKey());
                    sightingsList.add(0, add);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateMap() {
        googleMap.clear();
        List<RatSighting> sightings = getSightings();
        for (RatSighting sighting: sightings) {
            if (!(sighting.getLongitude().isEmpty() || sighting.getLatitude().isEmpty())) {
                LatLng location = new LatLng(Double.parseDouble(sighting.getLatitude()),
                        Double.parseDouble(sighting.getLongitude()));
                googleMap.addMarker(new MarkerOptions().position(location)
                        .title(sighting.getUniqueKey()));
            }
        }

    }

    // Pranav Bokey - responsible for testing this method
    /**
     * gets List of number of rat sightings
     * @return List of rat sightings
     */
    public List<RatSighting> getSightings() {
        List<RatSighting> sightings = MainActivity.sightingsList;
        List<RatSighting> ret = new LinkedList<>();
        if (start.compareTo(end) > 0) {
            return ret;
        }
        if (start.getYear() < 2015) {
            start = new Date(2015 - offset, 1, 1);
        }
        if (end.getYear() < 2015) {
            end = new Date(2015 - offset, 3, 1);
        }
        for (RatSighting sighting : sightings) {
            Date date;
            try {
                date = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.US).
                        parse(sighting.getCreatedDate());
            } catch (ParseException e) {
                date = new Date();
            }
            if ((date.compareTo(start) >= 0) && (date.compareTo(end) <= 0)) {
                // between start and end
                if (!(sighting.getLongitude().isEmpty() || sighting.getLatitude().isEmpty())) {
                    //LatLng location = new LatLng(Double.parseDouble(sighting.getLatitude()),
                    //        Double.parseDouble(sighting.getLongitude()));
                    ret.add(sighting);
                }
            }
        }
        return ret;
    }

    /**
     * gets List of rat sightings
     * @return List of rat sightings
     */
    public List<RatSighting> getSightingsList() {
        return sightingsList;
    }

    /**
     * Sets start date
     * @param x date to set start date
     */
    public void setStart(Date x) {
        start = x;
    }

    /**
     * Sets end date
     * @param x date to set end date
     */
    public void setEnd(Date x) {
        end = x;
    }


    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        int mYear;
        int mMonth;
        int mDay;
        final int date;
        // if date is 0, then we are updating the start date, else we are updating the end variable
        public mDateSetListener(int date) {
            this.date = date;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // getCalender();
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
//            String add = mMonth + "/" + mDay + "/" + mYear + " 00:00:00 0";
            if (date == 0) {
                start = new Date(mYear - offset, mMonth, mDay);
            } else {
                end = new Date(mYear - offset, mMonth, mDay);
            }
//            updateMap();
//            date.setYear(mYear);
//            date.setMonth(mMonth);
//            date.setDate(mDay);
            updateMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("BOKEY", "on map ready");
        this.googleMap = googleMap;
        updateMap();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7128, -74.0060),
                11.0f));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();

    }
}
