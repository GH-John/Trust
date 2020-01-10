package com.application.arenda.MainWorkspace.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Registration.RegistrationUser;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetDrawableImageViews;
import com.application.arenda.UI.SetStyle.SetBtnStyle;
import com.application.arenda.UI.SetStyle.SetFieldStyle;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

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

    private RegistrationUser registrationUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializationComponents();
        initializationStyles();
        initializationListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        registrationUser.requestCancel();
    }

    private void initializationComponents() {
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

        registrationUser = new RegistrationUser(this, new ActivityMain());
    }

    private void initializationStyles() {
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

    private void initializationListeners() {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(getResources().getString(R.string.hint_phone));
        FormatWatcher formatWatcher = new MaskFormatWatcher(
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(fieldPhoneReg);

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
                registrationUser.setProgressBar(progressBarReg);

                registrationUser.registration(fieldNameReg.getText().toString().trim(),
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