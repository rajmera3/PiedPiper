package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by pbokey on 9/25/17.
 * Start screen for the app
 */

public class SplashScreenActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private AccessToken facebookAccessToken;
    private LoginButton btnLoginFacebook;
    private FirebaseAuth firebaseAuth;
    private AccessToken accessToken;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Button loginButton = findViewById(R.id.login_button_id);
        Button registerButton = findViewById(R.id.register_button_id);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        btnLoginFacebook = (LoginButton) findViewById(R.id.facebook_button);
        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook.setReadPermissions("email");
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookAccessToken = loginResult.getAccessToken();
                Toast.makeText(getBaseContext(), "lil uzi loading", Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Facebook Login Error", "CANCELLED");
                Toast.makeText(getApplicationContext(), "Facebook login was cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook Login Error", "" + error);
                Toast.makeText(getApplicationContext(), "There was an error with the facebook login", Toast.LENGTH_SHORT).show();
            }
        });
        if (firebaseAuth.getCurrentUser() != null) {
            Log.d("already logged in", "switching to main activity");
            Intent main = new Intent(getBaseContext(), MainActivity.class);
            startActivity(main);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FACEBOOK ACCESS", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("BOKEY", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            database = FirebaseDatabase.getInstance().getReference();
                            if (!(database.child("users").child(user.getUid()) == null)) {
                                database.child("users").child(user.getUid())
                                        .setValue("User");
                            }

                            Toast.makeText(getApplicationContext(), "we gucci", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("BOKEY FAILED", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
