package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String accountType;
    private DatabaseReference database;
    public static List<RatSighting> sightingsList;
//    private DatabaseReference dataRef;
    public Query snap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sightingsList = new LinkedList<>();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        Button logoutButton = (Button) findViewById(R.id.logout_button_id);
        Button ratSighting = (Button) findViewById(R.id.ratlistview_button_id);
        Button addRatSighting = (Button) findViewById(R.id.addratsighting_button_id);
        Button sightingsMapButton = (Button) findViewById(R.id.sightingsmap_button_id);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auth.signOut();
            }
        });
        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent logout = new Intent(getBaseContext(), SplashScreenActivity.class);
                    startActivity(logout);
                    finish();
                }
            }
        };
        auth.addAuthStateListener(authListener);

        ratSighting.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent ratList = new Intent(getBaseContext(), RatSightingsActivity.class);
               startActivity(ratList);
               finish();
           }
        });

        addRatSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newSighting = new Intent(getBaseContext(), NewRatSightingActivity.class);
                startActivity(newSighting);
                finish();
            }
        });

        sightingsMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sightingsMap = new Intent(getBaseContext(), SightingsMapActivity.class);
                startActivity(sightingsMap);
                finish();
            }
        });

        // set account type from database
        database.child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountType = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        dataRef = database.child("sightings");

        snap = database.child("sightings").limitToLast(500);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("sunwoo", "started main");

        snap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("sunwoo", "added to list");
                sightingsList.clear();
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
                    add.setUniqueKey(sightingSnapshot.getKey());
                    sightingsList.add(0, add);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
