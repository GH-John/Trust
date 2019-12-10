package com.application.arenda.EntrySystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.CustomComponents.SetStyle.SetBtnStyle;
import com.application.arenda.CustomComponents.ComponentBackground;
import com.application.arenda.CustomComponents.SetStyle.SetFieldStyle;
import com.application.arenda.CustomComponents.SetDrawableImageViews;
import com.application.arenda.MainWorkspace.Activities.ActivityMain;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.Authorization.AuthorizationUser;

public class ActivityAuthorization extends AppCompatActivity {
    private EditText fieldEmailAuth, fieldPassAuth;
    private ImageView imagePinAuth;
    private Button btnSignAuth, btnRegAuth;
    private ProgressBar progressBarAuth;

    private AuthorizationUser authorizationUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initializationComponents();
        initializationStyles();
        initializationListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        authorizationUser.requestCancel();
    }

    private void initializationComponents() {
        fieldEmailAuth = findViewById(R.id.fieldEmailAuth);
        fieldPassAuth = findViewById(R.id.fieldPassAuth);

        imagePinAuth = findViewById(R.id.imagePinAuth);

        btnRegAuth = findViewById(R.id.btnRegAuth);
        btnSignAuth = findViewById(R.id.btnSignAuth);

        progressBarAuth = findViewById(R.id.progressBarAuth);

        authorizationUser = new AuthorizationUser(this, new ActivityMain());
    }

    private void initializationStyles() {
        SetDrawableImageViews.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 3f, 3f, 0f, 20f, 20f, 0f), imagePinAuth);

        SetFieldStyle.setEditTextBackground(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), fieldEmailAuth, fieldPassAuth);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnRegAuth);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnSignAuth);
    }

    private void initializationListeners() {
        btnRegAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityAuthorization.this, ActivityRegistration.class));
            }
        });

        btnSignAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fieldIsEmpty(fieldEmailAuth, fieldPassAuth) && !fieldIsIncorrectChars(fieldEmailAuth, fieldPassAuth)) {
                    authorizationUser.setProgressBar(progressBarAuth);

                    authorizationUser.authorization(
                            fieldEmailAuth.getText().toString().trim(),
                            fieldPassAuth.getText().toString().trim());
                }
            }
        });
    }

    public boolean fieldIsEmpty(EditText... fields) {
        int countEmpty = 0;
        if (fields.length > 0) {
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

    public boolean fieldIsIncorrectChars(EditText... fields) {
        int countIncorrectFields = 0;
        char[] incorrectChars = {' '};
        if (fields.length > 0) {
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