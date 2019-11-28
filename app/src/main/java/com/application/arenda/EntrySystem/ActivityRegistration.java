package com.application.arenda.EntrySystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.CustomComponents.BtnStyle.BtnBackground;
import com.application.arenda.CustomComponents.BtnStyle.SetBtnStyle;
import com.application.arenda.CustomComponents.FieldStyle.FieldBackground;
import com.application.arenda.CustomComponents.FieldStyle.SetFieldStyle;
import com.application.arenda.CustomComponents.PinStyle.PinBackground;
import com.application.arenda.CustomComponents.PinStyle.SetPinStyle;
import com.application.arenda.ServerInteraction.Registration.RegistrationUser;
import com.application.arenda.R;
import com.application.arenda.MainWorkspace.Activities.ActivityMain;

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

    private FieldBackground fieldBackground;
    private PinBackground pinBackground;
    private BtnBackground btnBackground;

    private RegistrationUser registrationUser;
    private static String URL_REGISTRATION = "http://192.168.43.241/AndroidConnectWithServer/php/authentification/RegistrationUser.php";

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

        registrationUser = new RegistrationUser(this, URL_REGISTRATION, new ActivityMain());
    }

    private void initializationStyles(){
        pinBackground = new PinBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{0f, 0f, 20f, 20f, 20f, 20f, 0f, 0f});

        SetPinStyle.setStyle(pinBackground, imagePinReg);

        fieldBackground = new FieldBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetFieldStyle.setEditTextBackground(fieldBackground, fieldNameReg,
                fieldLastNameReg,
                fieldEmailReg,
                fieldPassReg,
                fieldPhoneReg,
                fieldCodeReg);

        btnBackground = new BtnBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetBtnStyle.setStyle(btnBackground, btnReg);

        btnBackground = new BtnBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetBtnStyle.setStyle(btnBackground, btnGetCodeReg);
    }

    private void initializationListeners() {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(getResources().getString(R.string.hint_phone));
        FormatWatcher formatWatcher = new MaskFormatWatcher(
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(fieldPhoneReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fieldIsEmpty(fieldNameReg,
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
            }
        });
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