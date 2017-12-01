package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static int SIGHTINGS_LIMIT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Button logoutButton = findViewById(R.id.logout_button_id);
        Button ratSighting = findViewById(R.id.ratlistview_button_id);
        Button addRatSighting = findViewById(R.id.addratsighting_button_id);
        Button sightingsMapButton = findViewById(R.id.sightingsmap_button_id);
        Button sightingsGraphButton = findViewById(R.id.sightingsgraph_button_id);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                    Toast.makeText(getBaseContext(), "Logged out of Facebook", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Logged out of App", Toast.LENGTH_SHORT).show();
                }
                final SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                final String format = s.format(new Date());
                database.child("logging").child("logout").child(auth.getCurrentUser().getUid()).setValue("LOGOUT SUCCESSFUL: " + format);
                auth.signOut();
                Intent splash = new Intent(getBaseContext(), SplashScreenActivity.class);
                startActivity(splash);
                finish();
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
                    return;
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
        if (snap == null) {
            Intent splash = new Intent(getBaseContext(), SplashScreenActivity.class);
            startActivity(splash);
            finish();
            return;
        }
        updateSightingList();

    }

    public static void updateSightingList() {
       Query snapStatic = FirebaseDatabase.getInstance().getReference().child("sightings").limitToLast(SIGHTINGS_LIMIT);
        snapStatic.addValueEventListener(new ValueEventListener() {
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
