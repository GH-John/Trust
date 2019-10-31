package com.application.trust.PreviewApp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.application.trust.EntrySystem.Activities.ActivityAuthorization;
import com.application.trust.R;

public class ActivityPreview extends AppCompatActivity {

    private Button btnSkip;
    private LinearLayout dotsLayout;
    private ViewPager previewViewPager;
    private AdapterPreview adapterPreview;

    private ImageView[] dots;
    private Drawable dotSelected;
    private Drawable dotUnselected;
    private ViewPager.OnPageChangeListener pageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        initializationComponents();
        initializationListeners();

        setAdapter();
        setStartIndicator();
    }

    private void initializationComponents() {
        previewViewPager = findViewById(R.id.previewViewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
        btnSkip = findViewById(R.id.btnSkip);
        dotSelected = this.getDrawable(R.drawable.dot_selected);
        dotUnselected = this.getDrawable(R.drawable.dot_unselected);
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
                changeIndicator(position, dotUnselected, dotSelected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        previewViewPager.addOnPageChangeListener(pageListener);
    }

    private void setAdapter() {
        adapterPreview = new AdapterPreview(this);
        previewViewPager.setAdapter(adapterPreview);
    }

    public void createArrayDotsIndicator(int setDotCount, Drawable drawable) {
        LinearLayout.LayoutParams layoutParams;
        dots = new ImageView[setDotCount];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(drawable);
            dotsLayout.addView(dots[i]);
            layoutParams = new LinearLayout.LayoutParams(dots[i].getLayoutParams());
            layoutParams.leftMargin = 20;
            dots[i].setLayoutParams(layoutParams);
        }
    }

    public void changeIndicator(int position, Drawable dotUnselected, Drawable dotSelected) {
        if (dots.length > 0) {
            if (position > 0) {
                dots[position - 1].setImageDrawable(dotUnselected);
                dots[position].setImageDrawable(dotSelected);
                if (position < dots.length - 1) {
                    dots[position + 1].setImageDrawable(dotUnselected);
                }
            } else if (position == 0) {
                dots[position].setImageDrawable(dotSelected);
                dots[position + 1].setImageDrawable(dotUnselected);
            }
        }
    }

    private void setStartIndicator() {
        createArrayDotsIndicator(adapterPreview.getCount(),
                dotUnselected);
        changeIndicator(0, dotUnselected, dotSelected);
    }
}