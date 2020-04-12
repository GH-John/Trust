package com.application.arenda.MainWorkspace.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Authentication.Authentication;
import com.application.arenda.Entities.User.AccountType;
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

                authentication.registration(fieldNameReg.getText().toString().trim(),
                        fieldLastNameReg.getText().toString().trim(),
                        fieldLoginReg.getText().toString().trim(),
                        fieldEmailReg.getText().toString().trim(),
                        fieldPassReg.getText().toString().trim(),
                        fieldPhoneReg.getText().toString().trim(),
                        getAccountType(),
                        new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                progressBarReg.setVisibility(View.INVISIBLE);

                                onBackPressed();
                                finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                progressBarReg.setVisibility(View.INVISIBLE);

                                Utils.messageOutput(getApplicationContext(), "Error: - " + fault.getMessage());
                                Timber.e(fault.getMessage());
                            }
                        });
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