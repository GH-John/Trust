package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Authentication.Authentication;
import com.application.arenda.Entities.Authentication.OnAuthenticationListener;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetDrawableImageViews;
import com.application.arenda.UI.Style.SetBtnStyle;
import com.application.arenda.UI.Style.SetFieldStyle;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthException;

import timber.log.Timber;

public class ActivityAuthorization extends AppCompatActivity {
    private EditText fieldEmailAuth, fieldPassAuth;
    private ImageView imagePinAuth;
    private Button btnSignAuth, btnRegAuth;
    private ProgressBar progressBarAuth;

    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initComponents();
        initStyles();
        initListeners();
    }

    private void initComponents() {
        fieldEmailAuth = findViewById(R.id.fieldEmailAuth);
        fieldPassAuth = findViewById(R.id.fieldPassAuth);

        imagePinAuth = findViewById(R.id.imagePinAuth);

        btnRegAuth = findViewById(R.id.btnRegAuth);
        btnSignAuth = findViewById(R.id.btnSignAuth);

        progressBarAuth = findViewById(R.id.progressBarAuth);

        authentication = Authentication.getInstance();
    }

    private void initStyles() {
        SetDrawableImageViews.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 3f, 3f, 0f, 20f, 20f, 0f), imagePinAuth);

        SetFieldStyle.setEditTextBackground(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), fieldEmailAuth, fieldPassAuth);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnRegAuth);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnSignAuth);
    }

    private void initListeners() {
        authentication.setOnAuthorizationListener(new OnAuthenticationListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    progressBarAuth.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(ActivityAuthorization.this, ActivityMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e);

                progressBarAuth.setVisibility(View.INVISIBLE);

                if (e instanceof FirebaseNetworkException)
                    Utils.messageOutput(ActivityAuthorization.this, getString(R.string.error_check_internet_connect));
                else if (e instanceof FirebaseAuthException) {
                    switch (((FirebaseAuthException) e).getErrorCode()) {
                        case "ERROR_USER_NOT_FOUND":
                            fieldEmailAuth.setError(getString(R.string.error_user_not_found));
                            break;
                        case "ERROR_USER_DISABLED":
                            Utils.messageOutput(ActivityAuthorization.this, getString(R.string.error_user_disabled));
                            break;
                        case "ERROR_INVALID_EMAIL":
                            fieldEmailAuth.setError(getString(R.string.error_incorrect_format_email));
                            break;
                        case "ERROR_WEAK_PASSWORD":
                            fieldPassAuth.setError(getString(R.string.error_weak_password));
                            break;
                        case "ERROR_WRONG_PASSWORD":
                            fieldPassAuth.setError(getString(R.string.error_incorrect_password));
                            break;
                        default:
                            Utils.messageOutput(ActivityAuthorization.this, getString(R.string.unknown_error));
                            break;
                    }
                }
            }
        });

        btnRegAuth.setOnClickListener(v -> startActivity(new Intent(ActivityAuthorization.this, ActivityRegistration.class)));

        btnSignAuth.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getApplicationContext(), fieldEmailAuth, fieldPassAuth)) {
                progressBarAuth.setVisibility(View.VISIBLE);

                authentication.authorization(
                        fieldEmailAuth.getText().toString().trim(),
                        fieldPassAuth.getText().toString().trim());
            }
        });
    }
}