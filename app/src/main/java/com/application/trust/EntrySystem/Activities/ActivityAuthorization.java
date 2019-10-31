package com.application.trust.EntrySystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.EntrySystem.Authorization.AuthorizationUser;
import com.application.trust.R;
import com.application.trust.Workspace.Activities.ActivityMain;

public class ActivityAuthorization extends AppCompatActivity {
    private EditText fieldEmailAuth, fieldPassAuth;

    private Button btnSignAuth, btnRegAuth;

    private AuthorizationUser authorizationUser;
    private static String URL_AUTHORIZATION = "http://192.168.43.241/php/AuthorizationUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initializationComponents();
        initializationListeners();
    }

    private void initializationComponents(){
        fieldEmailAuth = findViewById(R.id.fieldEmailAuth);
        fieldPassAuth = findViewById(R.id.fieldPassAuth);

        btnRegAuth = findViewById(R.id.btnRegAuth);
        btnSignAuth = findViewById(R.id.btnSignAuth);

        authorizationUser = new AuthorizationUser(this, URL_AUTHORIZATION);
    }

    private void initializationListeners(){
        btnRegAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityAuthorization.this, ActivityRegistration.class));
            }
        });

        btnSignAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean statusAuth = authorizationUser.authorization(
                        fieldEmailAuth.getText().toString().trim(),
                        fieldPassAuth.getText().toString().trim());

                if(statusAuth) {
                    startActivity(new Intent(ActivityAuthorization.this, ActivityMain.class));
                }
            }
        });
    }
}