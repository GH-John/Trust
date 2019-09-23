package com.application.trust.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.application.trust.R;
import com.application.trust.StructureProject.ButtonsNavigation.Panels;

import java.util.Objects;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

public class ActivityMain extends AppCompatActivity {

    ImageView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        test = findViewById(R.id.testImage);
//
//        test.setImageDrawable(new Panels(this, test, R.color.colorWhite, FILL));
    }
}
