package com.application.arenda.MainWorkspace.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.Entities.SystemPreview.AdapterSystemPreview;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.Style.SetBtnStyle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityPreview extends AppCompatActivity {

    @Nullable
    @BindView(R.id.btnSkip)
    Button btnSkip;

    @Nullable
    @BindView(R.id.dotsLayout)
    LinearLayout dotsLayout;

    @Nullable
    @BindView(R.id.previewViewPager)
    ViewPager previewViewPager;

    private AdapterSystemPreview adapterSystemPreview;
    private ComponentBackground componentBackground;
    private ViewPager.OnPageChangeListener pageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        initializationStyles();
        initializationListeners();
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
                adapterSystemPreview.changeIndicator(position,
                        getDrawable(R.drawable.ic_dot_unselected), getDrawable(R.drawable.ic_dot_selected));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        previewViewPager.addOnPageChangeListener(pageListener);

        adapterSystemPreview = new AdapterSystemPreview(this, dotsLayout,
                getDrawable(R.drawable.ic_dot_unselected), getDrawable(R.drawable.ic_dot_selected));
        previewViewPager.setAdapter(adapterSystemPreview);
    }
}