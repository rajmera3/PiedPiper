package com.piedpiper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbokey on 10/9/17.
 */

public class RatSightingsActivity extends AppCompatActivity {

    private ListView list;
    private SightingList adapter;
    private DatabaseReference dataRef;
    private List<RatSighting> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratsightings);

        list = (ListView) findViewById(R.id.RatSightingListView);

        itemList = new ArrayList<>();

        dataRef = FirebaseDatabase.getInstance().getReference("sightings");
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for(DataSnapshot sightingSnapshot: dataSnapshot.getChildren()) {
                    RatSighting add = new RatSighting();
                    add.setCity((String) sightingSnapshot.child("City").getValue());
                    add.setBorough((String) sightingSnapshot.child("Borough").getValue());
                    add.setIncidentAddress((String) sightingSnapshot.child("Incident Address").getValue());
                    add.setIncidentZip((String) sightingSnapshot.child("Incident Zip").getValue());
                    add.setCreatedDate((String) sightingSnapshot.child("Created Date").getValue());
                    add.setLocationType((String) sightingSnapshot.child("Location Type").getValue());
                    add.setLatitude((String) sightingSnapshot.child("Latitude").getValue());
                    add.setLongitude((String) sightingSnapshot.child("Longitude").getValue());
                    add.setUniqueKey((String) sightingSnapshot.child("Unique Key").getValue());
                    itemList.add(add);
                }
                adapter = new SightingList(RatSightingsActivity.this, itemList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
