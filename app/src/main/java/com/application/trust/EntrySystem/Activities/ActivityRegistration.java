package com.application.trust.EntrySystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.EntrySystem.Registration.RegistrationUser;
import com.application.trust.R;
import com.application.trust.Workspace.Activities.ActivityMain;

public class ActivityRegistration extends AppCompatActivity {
    private RadioGroup radioGroup;

    private ProgressBar progressBarReg;

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
    private static String URL_REGISTRATION = "http://192.168.43.241/php/RegistrationUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializationComponents();
        initializationListeners();
    }

    private void initializationComponents() {
        radioGroup = findViewById(R.id.radioGroup);

        progressBarReg = findViewById(R.id.progressBarReg);

        fieldLastNameReg = findViewById(R.id.fieldLastNameReg);
        fieldEmailReg = findViewById(R.id.fieldEmailReg);
        fieldPhoneReg = findViewById(R.id.fieldPhoneReg);
        fieldNameReg = findViewById(R.id.fieldNameReg);
        fieldPassReg = findViewById(R.id.fieldPassReg);
        fieldCodeReg = findViewById(R.id.fieldCodeReg);

        btnGetCodeReg = findViewById(R.id.btnGetCodeReg);
        btnReg = findViewById(R.id.btnReg);

        registrationUser = new RegistrationUser(this, URL_REGISTRATION);
    }

    private void initializationListeners() {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                progressBarReg.setVisibility(View.VISIBLE);

                boolean statusReg = registrationUser.registration(fieldNameReg.getText().toString().trim(),
                        fieldLastNameReg.getText().toString().trim(),
                        fieldEmailReg.getText().toString().trim(),
                        fieldPassReg.getText().toString().trim(),
                        fieldPhoneReg.getText().toString().trim(),
                        getAccountType());

                progressBarReg.setVisibility(View.GONE);

                if(statusReg) {
                    startActivity(new Intent(ActivityRegistration.this, ActivityMain.class));
                }
            }
        });
    }

    private String getAccountType(){
        String type = "private person";

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rBtnPrivatePerson:
                type = "private person";
            case R.id.rBtnBusiness: {
                type = "business";
                break;
            }
        }
        return type;
    }
}