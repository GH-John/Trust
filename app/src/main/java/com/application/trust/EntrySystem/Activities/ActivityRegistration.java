package com.application.trust.EntrySystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.CustomComponents.BtnStyle.BtnBackground;
import com.application.trust.CustomComponents.BtnStyle.SetBtnStyle;
import com.application.trust.CustomComponents.FieldStyle.FieldBackground;
import com.application.trust.CustomComponents.FieldStyle.SetFieldStyle;
import com.application.trust.CustomComponents.PinStyle.PinBackground;
import com.application.trust.CustomComponents.PinStyle.SetPinStyle;
import com.application.trust.EntrySystem.Registration.RegistrationUser;
import com.application.trust.R;
import com.application.trust.Workspace.Activities.ActivityMain;

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
    private static String URL_REGISTRATION = "http://192.168.43.241/AndroidConnectWithServer/php/RegistrationUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializationComponents();
        initializationStyle();
        initializationListeners();
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

    private void initializationStyle(){
        pinBackground = new PinBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{0f, 0f, 20f, 20f, 20f, 20f, 0f, 0f});

        SetPinStyle.setStyle(pinBackground, imagePinReg);

        fieldBackground = new FieldBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetFieldStyle.setStyle(fieldBackground, fieldNameReg,
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

    @Override
    protected void onStop() {
        super.onStop();
        registrationUser.requestCancel();
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
            case R.id.rBtnPrivatePerson: {
                type = "person";
                break;
            }
            case R.id.rBtnBusiness: {
                type = "business";
                break;
            }
        }
        return type;
    }
}