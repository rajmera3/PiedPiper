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
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String accountType;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        Button logoutButton = (Button) findViewById(R.id.logout_button_id);
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
                System.out.println("signed out");
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

        // set account type from database
        database.child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountType = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
