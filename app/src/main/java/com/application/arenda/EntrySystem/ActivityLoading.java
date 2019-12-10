package com.application.arenda.EntrySystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.Cookies.UserCookie;
import com.application.arenda.CustomComponents.SetStyle.SetBtnStyle;
import com.application.arenda.CustomComponents.ComponentBackground;
import com.application.arenda.MainWorkspace.Activities.ActivityMain;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.PreviewSystem.ActivityPreview;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.Authorization.AuthorizationUseToken;

public class ActivityLoading extends AppCompatActivity implements Observer {
    private Button btnReconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        initializationComponents();
        initializationListeners();
        initializationStyles();
    }

    private void initializationComponents() {
        btnReconnect = findViewById(R.id.btnReconnect);
    }

    private void initializationListeners() {
        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReconnect.setVisibility(View.INVISIBLE);
                new AuthorizationUseToken(getApplicationContext()).authorization(UserCookie.getToken(getApplicationContext()), ActivityLoading.this);
            }
        });
    }

    private void initializationStyles() {
        SetBtnStyle.setStyle(new ComponentBackground(getApplicationContext(), R.color.colorAccent, R.color.shadowColor,
                6f, 0f, 3f, 20f), btnReconnect);
    }

    private void checkToken() {
        String token = UserCookie.getToken(getApplicationContext());
        if (token.isEmpty())
            startActivity(new Intent(ActivityLoading.this, ActivityPreview.class));
        else {
            new AuthorizationUseToken(getApplicationContext()).authorization(token, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkToken();
    }

    @Override
    public void update(@NonNull Object object) {
        if (object instanceof Boolean) {
            if ((Boolean) object)
                startActivity(new Intent(ActivityLoading.this, ActivityMain.class));
            else btnReconnect.setVisibility(View.VISIBLE);
        }
    }
}