package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.Entities.SystemPreview.AdapterSystemPreview;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetStyle.SetBtnStyle;
import com.application.arenda.R;

public class ActivityPreview extends AppCompatActivity {

    private Button btnSkip;
    private LinearLayout dotsLayout;
    private ViewPager previewViewPager;
    private AdapterSystemPreview adapterSystemPreview;

    private Drawable dotSelected;
    private Drawable dotUnselected;
    private ComponentBackground componentBackground;
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
        dotSelected = this.getDrawable(R.drawable.preview_dot_selected);
        dotUnselected = this.getDrawable(R.drawable.preview_dot_unselected);
    }

    private void initializationStyles() {
        componentBackground = new ComponentBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f);

        SetBtnStyle.setStyle(componentBackground, btnSkip);
    }

    private void initializationListeners() {
        btnSkip.setOnClickListener(v -> startActivity(new Intent(ActivityPreview.this, ActivityAuthorization.class)));

        pageListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapterSystemPreview.changeIndicator(position, dotUnselected, dotSelected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        previewViewPager.addOnPageChangeListener(pageListener);
    }

    private void setAdapter() {
        adapterSystemPreview = new AdapterSystemPreview(this, dotsLayout, dotUnselected, dotSelected);
        previewViewPager.setAdapter(adapterSystemPreview);
    }
}