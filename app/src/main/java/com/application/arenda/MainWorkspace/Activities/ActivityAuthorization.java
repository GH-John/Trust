package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Authentication.Authentication;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetDrawableImageViews;
import com.application.arenda.UI.Style.SetBtnStyle;
import com.application.arenda.UI.Style.SetFieldStyle;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

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
        btnRegAuth.setOnClickListener(v -> startActivity(new Intent(ActivityAuthorization.this, ActivityRegistration.class)));

        itemForgotPass.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getApplicationContext(), fieldEmailAuth))
                authentication.restorePassword(fieldEmailAuth.getText().toString(), new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        progressBarAuth.setVisibility(View.INVISIBLE);

                        Utils.messageOutput(getApplicationContext(), getResources().getString(R.string.instruction_send_to_email));
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progressBarAuth.setVisibility(View.INVISIBLE);

                        Utils.messageOutput(getApplicationContext(), "Error: - " + fault.getMessage());
                        Timber.e(fault.getMessage());
                    }
                });
        });

        btnSignAuth.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getApplicationContext(), fieldEmailAuth, fieldPassAuth)) {
                progressBarAuth.setVisibility(View.VISIBLE);

                authentication.authorization(
                        fieldEmailAuth.getText().toString().trim(),
                        fieldPassAuth.getText().toString().trim(),
                        new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                progressBarAuth.setVisibility(View.INVISIBLE);

                                onBackPressed();
                                finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                progressBarAuth.setVisibility(View.INVISIBLE);

                                Utils.messageOutput(getApplicationContext(), "Error: - " + fault.getMessage());
                                Timber.e(fault.getMessage());
                            }
                        });
            }
        });
    }
}