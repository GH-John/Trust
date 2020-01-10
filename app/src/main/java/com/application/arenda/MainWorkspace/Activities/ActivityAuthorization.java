package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Authorization.AuthorizationUser;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetDrawableImageViews;
import com.application.arenda.UI.SetStyle.SetBtnStyle;
import com.application.arenda.UI.SetStyle.SetFieldStyle;

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
        btnRegAuth.setOnClickListener(v -> startActivity(new Intent(ActivityAuthorization.this, ActivityRegistration.class)));

        btnSignAuth.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getApplicationContext(), fieldEmailAuth, fieldPassAuth)) {
                authorizationUser.setProgressBar(progressBarAuth);

                authorizationUser.authorization(
                        fieldEmailAuth.getText().toString().trim(),
                        fieldPassAuth.getText().toString().trim());
            }
        });
    }
}