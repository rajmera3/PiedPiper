package com.piedpiper;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.text.SimpleDateFormat;
        import java.util.Date;
/**
 * Created by pbokey on 9/22/17.
 * Activity for handling Login
 */

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference success = dataRef.child("logging").child("success");
        final DatabaseReference failure = dataRef.child("logging").child("failure");
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        final Button loginButton = findViewById(R.id.login_button_id);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = ((EditText) findViewById(R.id.username_text_input)).getText()
                        .toString().trim().toLowerCase();
                String password = ((EditText) findViewById(R.id.password_text_input)).getText()
                        .toString();
                String loginAction = login(userName, password);

                if ("No credentials".equals(loginAction)) {
                    Toast.makeText(getApplicationContext(), "Enter email address and password!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("No username".equals(loginAction)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("No password".equals(loginAction)) {
                    Toast.makeText(getApplicationContext(), "Enter password!",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    final SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    final String format = s.format(new Date());
                    auth.signInWithEmailAndPassword(userName, password)
                            .addOnCompleteListener(LoginActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                // this block of code is supposed to sign in to the database anonomously and
                                // then update the failure child in the logging table with a new row
                                // however I cannot get this to work for some reason
                                //TODO: Fix this method to update the logigng table with a failure
                                auth.signInAnonymously()
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    failure.child(userName).push().setValue("LOGIN FAILED: " + format);
                                                    auth.signOut();
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    auth.signOut();
                                                }
                                            }
                                        });

                                //make an error message if you register with the same email
                                AlertDialog alertDialog =
                                        new AlertDialog.Builder(LoginActivity.this).create();
                                alertDialog.setTitle("Incorrect Login Information");
                                alertDialog.setMessage("Please provide correct credentials");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            } else {

                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = currentUser.getUid();
                                success.child(uid).push().setValue("LOGIN SUCCESSFUL: " + format);
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }); }
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button_id);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splash = new Intent(getBaseContext(), SplashScreenActivity.class);
                startActivity(splash);
            }
        });
    }

    /**
     * Method to determine whether or not to try logging in
     * @param userName login credential
     * @param password login credential
     * @return String to determine what message to show or to login
     */
    public String login(String userName, String password) {
//        if (TextUtils.isEmpty(userName)) {
//            Toast.makeText(getApplicationContext(),
// "Enter email address!", Toast.LENGTH_SHORT).show();
//            return "No username";
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
//            return "No password";
//        }
        if ("".equals(userName) && "".equals(password)) {
            return "No credentials";
        }

        if ("".equals(userName)) {
            return "No username";
        }

        if ("".equals(password)) {
            return "No password";
        }
        return "login";
    }



}