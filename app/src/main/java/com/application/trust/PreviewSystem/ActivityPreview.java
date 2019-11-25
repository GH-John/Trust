package com.application.trust.PreviewSystem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.application.trust.CustomComponents.BtnStyle.BtnBackground;
import com.application.trust.CustomComponents.BtnStyle.SetBtnStyle;
import com.application.trust.EntrySystem.ActivityAuthorization;
import com.application.trust.R;

public class ActivityPreview extends AppCompatActivity {

    private Button btnSkip;
    private LinearLayout dotsLayout;
    private ViewPager previewViewPager;
    private AdapterPreview adapterPreview;

    private Drawable dotSelected;
    private Drawable dotUnselected;
    private BtnBackground btnBackground;
    private ViewPager.OnPageChangeListener pageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        initializationComponents();
        initializationStyles();
        initializationListeners();

        setAdapter();
    }

    private void initializationComponents() {
        previewViewPager = findViewById(R.id.previewViewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
        btnSkip = findViewById(R.id.btnSkip);
        dotSelected = this.getDrawable(R.drawable.dot_selected);
        dotUnselected = this.getDrawable(R.drawable.dot_unselected);
    }

    private void initializationStyles() {
        btnBackground = new BtnBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetBtnStyle.setStyle(btnBackground, btnSkip);
    }

    private void initializationListeners() {
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityPreview.this, ActivityAuthorization.class));
            }
        });

        pageListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapterPreview.changeIndicator(position, dotUnselected, dotSelected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        previewViewPager.addOnPageChangeListener(pageListener);
    }

    private void setAdapter() {
        adapterPreview = new AdapterPreview(this, dotsLayout, dotUnselected, dotSelected);
        previewViewPager.setAdapter(adapterPreview);
    }
}