package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;
import timber.log.Timber;

public class ActivityRegistration extends AppCompatActivity {
    private ProgressBar progressBarReg;
    private RadioGroup radioGroup;
    private ImageView imagePinReg;
    private EditText
            fieldLastNameReg,
            fieldEmailReg,
            fieldPhoneReg,
            fieldNameReg,
            fieldPassReg,
            fieldCodeReg;
    private Button
            btnGetCodeReg,
            btnReg;

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

        fieldLastNameReg = findViewById(R.id.fieldLastNameReg);
        fieldEmailReg = findViewById(R.id.fieldEmailReg);
        fieldPhoneReg = findViewById(R.id.fieldPhoneReg);
        fieldNameReg = findViewById(R.id.fieldNameReg);
        fieldPassReg = findViewById(R.id.fieldPassReg);
        fieldCodeReg = findViewById(R.id.fieldCodeReg);

        btnGetCodeReg = findViewById(R.id.btnGetCodeReg);
        btnReg = findViewById(R.id.btnReg);

        authentication = Authentication.getInstance();
    }

    private void initStyles() {
        SetDrawableImageViews.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 3f, 3f, 0f, 20f, 20f, 0f), imagePinReg);

        SetFieldStyle.setEditTextBackground(new ComponentBackground(this, R.color.colorWhite,
                        R.color.shadowColor, 6f, 0f, 3f, 20f), fieldNameReg,
                fieldLastNameReg,
                fieldEmailReg,
                fieldPassReg,
                fieldPhoneReg,
                fieldCodeReg);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnGetCodeReg);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnReg);
    }

    private void initListeners() {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(getResources().getString(R.string.hint_phone));
        FormatWatcher formatWatcher = new MaskFormatWatcher(
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(fieldPhoneReg);

        authentication.setOnRegisterListener(new OnAuthenticationListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    progressBarReg.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(ActivityRegistration.this, ActivityMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e);

                progressBarReg.setVisibility(View.INVISIBLE);

                if (e instanceof FirebaseAuthUserCollisionException)
                    fieldEmailReg.setError(getString(R.string.error_user_exists));
                else if (e instanceof FirebaseNetworkException)
                    Utils.messageOutput(ActivityRegistration.this, getString(R.string.error_check_internet_connect));
                else if (e instanceof FirebaseAuthException) {
                    switch (((FirebaseAuthException) e).getErrorCode()) {
                        case "ERROR_INVALID_EMAIL":
                            fieldEmailReg.setError(getString(R.string.error_incorrect_format_email));
                            break;
                        case "ERROR_WEAK_PASSWORD":
                            fieldPassReg.setError(getString(R.string.error_weak_password));
                            break;
                        default:
                            Utils.messageOutput(ActivityRegistration.this, getString(R.string.unknown_error));
                            break;
                    }
                }
            }
        });

        btnReg.setOnClickListener(v -> {
            if (Utils.textIsAlphabet(getApplicationContext(),
                    fieldNameReg,
                    fieldLastNameReg) &&

                    Utils.isEmail(getApplicationContext(), fieldEmailReg) &&

                    !Utils.fieldIsEmpty(getApplicationContext(),
                            fieldNameReg,
                            fieldLastNameReg,
                            fieldEmailReg,
                            fieldPhoneReg,
                            fieldPassReg,
                            fieldCodeReg)) {
                progressBarReg.setVisibility(View.VISIBLE);

                authentication.registration(fieldNameReg.getText().toString().trim(),
                        fieldLastNameReg.getText().toString().trim(),
                        fieldEmailReg.getText().toString().trim(),
                        fieldPassReg.getText().toString().trim(),
                        fieldPhoneReg.getText().toString().trim(),
                        getAccountType());
            }
        });
    }

    private String getAccountType() {
        String type = "person";

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioBtnPrivatePerson: {
                type = "person";
                break;
            }
            case R.id.radioBtnBusiness: {
                type = "business";
                break;
            }
        }
        return type;
    }
}