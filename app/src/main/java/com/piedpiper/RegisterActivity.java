package com.piedpiper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbokey on 10/1/17.
 * Activity to handle user registration
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner accountTypeSpinner;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        Button btnRegister = findViewById(R.id.register_button_id);
        Button btnCancel = findViewById(R.id.cancel_button_id);

        inputEmail = findViewById(R.id.email_text_input);
        inputPassword = findViewById(R.id.password_text_input);

        accountTypeSpinner = findViewById(R.id.account_type_spinner);

        /*
          Set up the adapter to display the allowable majors in the spinner
         */
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Admin");
        spinnerItems.add("User");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter);

        // register the user on click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputEmail == null) {
                    AlertDialog alertDialog =
                            new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Email is not entered");
                    alertDialog.setMessage("Please enter an email");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                if ((inputPassword == null) || (inputPassword.toString().length() < 6)) {
                    AlertDialog alertDialog =
                            new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Password is not entered");
                    alertDialog.setMessage("Please enter a password that is at least 6 " +
                            "characters long");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                final String email = inputEmail.getText().toString().trim().toLowerCase();
                final String password = inputPassword.getText().toString();

                final String accountType = (String) accountTypeSpinner.getSelectedItem();

                // create the user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this,
                                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        // if user registers again with the same email, have error message
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                AlertDialog alertDialog =
                                        new AlertDialog.Builder(RegisterActivity.this).create();
                                alertDialog.setTitle("Register Failed");
                                alertDialog.setMessage("Please enter a password that is at " +
                                        "least 6 characters long");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                return;
                            }
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                AlertDialog alertDialog =
                                        new AlertDialog.Builder(RegisterActivity.this).create();
                                alertDialog.setTitle("Register Failed");
                                alertDialog.setMessage("This email already exists");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            } catch (Exception e) {
                                AlertDialog alertDialog =
                                        new AlertDialog.Builder(RegisterActivity.this).create();
                                alertDialog.setTitle("Register Failed");
                                alertDialog.setMessage(e.getMessage());
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        } else {
                            // add the user's account type to the database
                            database.child("users").child(auth.getCurrentUser().getUid())
                                    .setValue(accountType);

                            // alert that registration was successful
                            AlertDialog alertDialog =
                                    new AlertDialog.Builder(RegisterActivity.this).create();
                            alertDialog.setTitle("Successfully Registered");
                            alertDialog.setMessage("Registered new user!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            // go to the main page
                                            startActivity(new Intent(RegisterActivity.this,
                                                    MainActivity.class));
                                            finish();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
            }
        });

        // exit the registration page on cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splash = new Intent(getBaseContext(), SplashScreenActivity.class);
                startActivity(splash);
            }
        });

    }
}
