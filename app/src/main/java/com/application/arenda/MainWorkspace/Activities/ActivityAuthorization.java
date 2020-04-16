package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Authentication.ApiAuthentication;
import com.application.arenda.Entities.Authentication.Authentication;
import com.application.arenda.Entities.Authentication.OnAuthenticationListener;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetDrawableImageViews;
import com.application.arenda.UI.Style.SetBtnStyle;
import com.application.arenda.UI.Style.SetFieldStyle;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import timber.log.Timber;

public class ActivityAuthorization extends AppCompatActivity {
    private EditText fieldEmailAuth, fieldPassAuth;
    private TextView itemForgotPass;
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

        itemForgotPass = findViewById(R.id.itemForgotPass);

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
        itemForgotPass.setOnClickListener(v -> Utils.messageOutput(this, "In process developing"));

        authentication.setOnAuthenticationListener(new OnAuthenticationListener() {
            @Override
            public void onComplete(@NonNull ApiAuthentication.AuthenticationCodes code) {
                switch (code) {
                    case USER_LOGGED:
                        progressBarAuth.setVisibility(View.INVISIBLE);

                        onBackPressed();
                        finish();
                        break;

                    case WRONG_EMAIL:
                        progressBarAuth.setVisibility(View.INVISIBLE);
                        fieldEmailAuth.setError(getString(R.string.error_user_not_found));
                        break;

                    case WRONG_PASSWORD:
                        progressBarAuth.setVisibility(View.INVISIBLE);
                        fieldPassAuth.setError(getString(R.string.error_wrong_password));
                        break;

                    case UNKNOW_ERROR:
                    case NETWORK_ERROR:
                    case NOT_CONNECT_TO_DB:
                        progressBarAuth.setVisibility(View.INVISIBLE);
                        Utils.messageOutput(ActivityAuthorization.this, getString(R.string.error_check_internet_connect));
                        break;

                    default:
                        progressBarAuth.setVisibility(View.INVISIBLE);
                        Utils.messageOutput(ActivityAuthorization.this, getString(R.string.unknown_error));
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Timber.e(t);
                progressBarAuth.setVisibility(View.INVISIBLE);
                if (t instanceof SocketTimeoutException || t instanceof ConnectException) {
                    Utils.messageOutput(ActivityAuthorization.this, getString(R.string.error_check_internet_connect));
                }
            }
        });

        btnRegAuth.setOnClickListener(v -> startActivity(new Intent(ActivityAuthorization.this, ActivityRegistration.class)));

        btnSignAuth.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getApplicationContext(), fieldEmailAuth, fieldPassAuth)) {
                progressBarAuth.setVisibility(View.VISIBLE);

                authentication.authorization(this,
                        fieldEmailAuth.getText().toString().trim(),
                        fieldPassAuth.getText().toString().trim());
            }
        });
    }
}