package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

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
    }
}
