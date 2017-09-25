package com.piedpiper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by pbokey on 9/22/17.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button_id);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userName = ((EditText) findViewById(R.id.username_text_input)).getText().toString();
                String password = ((EditText) findViewById(R.id.password_text_input)).getText().toString();
                if (userName.equals("user") && password.equals("pass")) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Incorrect Login Information");
                    alertDialog.setMessage("Please provide correct credentials");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_button_id);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent splash = new Intent(getBaseContext(), SplashScreenActivity.class);
                startActivity(splash);
            }
        });
    }



}
