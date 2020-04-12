package com.application.arenda.MainWorkspace.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Authentication.ApiAuthentication;
import com.application.arenda.Entities.Authentication.Authentication;
import com.application.arenda.Entities.Authentication.OnAuthenticationListener;
import com.application.arenda.Entities.User.AccountType;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetDrawableImageViews;
import com.application.arenda.UI.Style.SetBtnStyle;
import com.application.arenda.UI.Style.SetFieldStyle;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import timber.log.Timber;

public class ActivityRegistration extends AppCompatActivity {
    private ProgressBar progressBarReg;
    private RadioGroup radioGroup;
    private ImageView imagePinReg;
    private EditText
            fieldNameReg,
            fieldLastNameReg,
            fieldLoginReg,
            fieldEmailReg,
            fieldPassReg,
            fieldConfirmPassReg,
            fieldPhoneReg;

    private Button btnReg;

    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initComponents();
        initStyles();
        initListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initComponents() {
        radioGroup = findViewById(R.id.radioGroup);

        imagePinReg = findViewById(R.id.imagePinReg);

        progressBarReg = findViewById(R.id.progressBarReg);

        fieldNameReg = findViewById(R.id.fieldNameReg);
        fieldLastNameReg = findViewById(R.id.fieldLastNameReg);

        fieldLoginReg = findViewById(R.id.fieldLoginReg);
        fieldEmailReg = findViewById(R.id.fieldEmailReg);

        fieldPassReg = findViewById(R.id.fieldPassReg);
        fieldConfirmPassReg = findViewById(R.id.fieldConfirmPassReg);

        fieldPhoneReg = findViewById(R.id.fieldPhoneReg);

        btnReg = findViewById(R.id.btnReg);

        authentication = Authentication.getInstance();

        Utils.setPhoneMask(getResources().getString(R.string.hint_phone), fieldPhoneReg);
    }

    private void initStyles() {
        SetDrawableImageViews.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 3f, 3f, 0f, 20f, 20f, 0f), imagePinReg);

        SetFieldStyle.setEditTextBackground(new ComponentBackground(this, R.color.colorWhite,
                        R.color.shadowColor, 6f, 0f, 3f, 20f), fieldNameReg,
                fieldLastNameReg,
                fieldLoginReg,
                fieldEmailReg,
                fieldPassReg,
                fieldConfirmPassReg,
                fieldPhoneReg);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnReg);
    }

    private void initListeners() {
        authentication.setOnAuthenticationListener(new OnAuthenticationListener() {
            @Override
            public void onComplete(@NonNull ApiAuthentication.AuthenticationCodes code) {
                switch (code) {
                    case USER_WITH_LOGIN_EXISTS:
                        progressBarReg.setVisibility(View.INVISIBLE);
                        fieldLoginReg.setError(getString(R.string.error_user_login_exists));
                        break;
                    case USER_EXISTS:
                        progressBarReg.setVisibility(View.INVISIBLE);
                        fieldEmailReg.setError(getString(R.string.error_user_exists));
                        break;
                    case USER_SUCCESS_REGISTERED:
                        progressBarReg.setVisibility(View.INVISIBLE);

                        onBackPressed();
                        finish();
                        break;

                    case USER_UNSUCCESS_REGISTERED:
                    case UNKNOW_ERROR:
                    case NETWORK_ERROR:
                    case NOT_CONNECT_TO_DB:
                        progressBarReg.setVisibility(View.INVISIBLE);
                        Utils.messageOutput(ActivityRegistration.this, getString(R.string.error_check_internet_connect));
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Timber.e(t);
                progressBarReg.setVisibility(View.INVISIBLE);
                if (t instanceof SocketTimeoutException || t instanceof ConnectException) {
                    Utils.messageOutput(ActivityRegistration.this, getString(R.string.error_check_internet_connect));
                }
            }
        });

        btnReg.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getApplicationContext(),
                    fieldNameReg,
                    fieldLastNameReg,
                    fieldLoginReg,
                    fieldEmailReg,
                    fieldPhoneReg,
                    fieldPassReg) &&

                    Utils.isEmail(getApplicationContext(), fieldEmailReg) &&

                    Utils.textIsAlphabet(getApplicationContext(),
                            fieldNameReg,
                            fieldLastNameReg) &&

                    !Utils.isWeakPassword(getApplicationContext(), fieldPassReg) &&

                    Utils.isConfirmPassword(getApplicationContext(), fieldPassReg, fieldConfirmPassReg)) {
                progressBarReg.setVisibility(View.VISIBLE);

                authentication.registration(this,
                        fieldNameReg.getText().toString().trim(),
                        fieldLastNameReg.getText().toString().trim(),
                        fieldLoginReg.getText().toString().trim(),
                        fieldEmailReg.getText().toString().trim(),
                        fieldPassReg.getText().toString().trim(),
                        fieldPhoneReg.getText().toString().trim(),
                        getAccountType());
            }
        });
    }

    private AccountType getAccountType() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioBtnPrivatePerson:
                return AccountType.PRIVATE_PERSON;

            case R.id.radioBtnBusiness:
                return AccountType.BUSINESS_PERSON;
        }

        return AccountType.PRIVATE_PERSON;
    }
}