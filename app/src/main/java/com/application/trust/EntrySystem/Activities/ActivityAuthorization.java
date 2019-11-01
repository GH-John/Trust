package com.application.trust.EntrySystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.CustomComponents.BtnStyle.BtnBackground;
import com.application.trust.CustomComponents.BtnStyle.SetBtnStyle;
import com.application.trust.CustomComponents.FieldStyle.FieldBackground;
import com.application.trust.CustomComponents.FieldStyle.SetFieldStyle;
import com.application.trust.CustomComponents.PinStyle.PinBackground;
import com.application.trust.CustomComponents.PinStyle.SetPinStyle;
import com.application.trust.EntrySystem.Authorization.AuthorizationUser;
import com.application.trust.R;
import com.application.trust.Workspace.Activities.ActivityMain;

public class ActivityAuthorization extends AppCompatActivity {
    private EditText fieldEmailAuth, fieldPassAuth;
    private ImageView imagePinAuth;
    private Button btnSignAuth, btnRegAuth;
    private ProgressBar progressBarAuth;

    private FieldBackground fieldBackground;
    private PinBackground pinBackground;
    private BtnBackground btnBackground;

    private AuthorizationUser authorizationUser;
    private static String URL_AUTHORIZATION = "http://192.168.43.241/AndroidConnectWithServer/php/AuthorizationUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initializationComponents();
        initializationStyle();
        initializationListeners();
    }

    private void initializationComponents(){
        fieldEmailAuth = findViewById(R.id.fieldEmailAuth);
        fieldPassAuth = findViewById(R.id.fieldPassAuth);

        imagePinAuth = findViewById(R.id.imagePinAuth);

        btnRegAuth = findViewById(R.id.btnRegAuth);
        btnSignAuth = findViewById(R.id.btnSignAuth);

        progressBarAuth = findViewById(R.id.progressBarAuth);

        authorizationUser = new AuthorizationUser(this, URL_AUTHORIZATION, new ActivityMain());
    }

    private void initializationStyle(){
        pinBackground = new PinBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{0f, 0f, 20f, 20f, 20f, 20f, 0f, 0f});

        SetPinStyle.setStyle(pinBackground, imagePinAuth);

        fieldBackground = new FieldBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetFieldStyle.setStyle(fieldBackground, fieldEmailAuth, fieldPassAuth);

        btnBackground = new BtnBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetBtnStyle.setStyle(btnBackground, btnSignAuth);

        btnBackground = new BtnBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetBtnStyle.setStyle(btnBackground, btnRegAuth);
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
                if(!fieldIsEmpty(fieldEmailAuth, fieldPassAuth) && !fieldIsIncorrectChars(fieldEmailAuth, fieldPassAuth)) {
                    authorizationUser.setProgressBar(progressBarAuth);

                    authorizationUser.authorization(
                            fieldEmailAuth.getText().toString().trim(),
                            fieldPassAuth.getText().toString().trim());
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        authorizationUser.requestCancel();
    }

    public boolean fieldIsEmpty(EditText ... fields){
        int countEmpty = 0;
        if(fields.length > 0) {
            for (EditText field : fields) {
                if (field.getText().toString().isEmpty()) {
                    field.setError(getResources().getString(R.string.error_empty_field));
                    countEmpty++;
                }
            }
            return countEmpty == fields.length || countEmpty > 0;
        }
        return true;
    }

    public boolean fieldIsIncorrectChars(EditText ... fields){
        int countIncorrectFields = 0;
        char[] incorrectChars = {' '};
        if(fields.length > 0) {
            for (char incoChars : incorrectChars) {
                for (EditText field : fields) {
                    if (field.getText().toString().matches(" ")) {
                        field.setError(getResources().getString(R.string.error_incorrect_chars));
                        countIncorrectFields++;
                    }
                }
            }
            return countIncorrectFields == fields.length || countIncorrectFields > 0;
        }
        return true;
    }
}