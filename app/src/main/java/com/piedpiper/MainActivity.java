package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Class for the main activity
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String accountType;
    public static final List<RatSighting> sightingsList = new LinkedList<>();
//    private DatabaseReference dataRef;
    private Query snap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Button logoutButton = findViewById(R.id.logout_button_id);
        Button ratSighting = findViewById(R.id.ratlistview_button_id);
        Button addRatSighting = findViewById(R.id.addratsighting_button_id);
        Button sightingsMapButton = findViewById(R.id.sightingsmap_button_id);
        Button sightingsGraphButton = findViewById(R.id.sightingsgraph_button_id);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
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

        sightingsGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sightingsGraph = new Intent(getBaseContext(), SightingsGraphActivity.class);
                startActivity(sightingsGraph);
                finish();
            }
        });

        // set account type from database
        database.child("users").child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountType = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        dataRef = database.child("sightings");

        int SIGHTINGS_LIMIT = 500;
        snap = database.child("sightings").limitToLast(SIGHTINGS_LIMIT);
    }

    @Override
    protected void onStart() {
        super.onStart();

        snap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sightingsList.clear();
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

    @Override
    public void onBackPressed() {

    }
}
