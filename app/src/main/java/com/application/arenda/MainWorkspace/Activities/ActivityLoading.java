package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Entities.Authentication.Authentication;
import com.application.arenda.R;

public class ActivityLoading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        checkUser();
    }

    private void checkUser() {
        Intent intent;

        if (Authentication.getCurrentUser() != null)
            intent = new Intent(ActivityLoading.this, ActivityMain.class);
        else
            intent = new Intent(ActivityLoading.this, ActivityPreview.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }
}