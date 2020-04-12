package com.application.arenda.UI.ContainerImg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.application.arenda.R;
import com.application.arenda.UI.ContainerImg.CustomViews.CustomViewImg;
import com.application.arenda.UI.ContainerImg.Galery.AdapterGalery;
import com.application.arenda.UI.ContainerImg.Galery.ChooseImages;

public class ContainerSelectedImages extends LinearLayout implements Container {
    private LinearLayout containerImg;
    private CardView btnGalery;
    private TextView counter;

    private AdapterGalery adapterGalery;
    private AdapterContainer adapterContainer;

    public ContainerSelectedImages(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
        initializationListeners();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.container_selected_img, this);
        btnGalery = findViewById(R.id.btnSelectPicture);
        containerImg = findViewById(R.id.containerImg);
    }

    private void initializationStyles() {
    }

    private void initializationListeners() {
        btnGalery.setOnClickListener(v -> ChooseImages.getInstance(adapterGalery).openGalery());
    }

    public void setAdapterGalery(AdapterGalery galery) {
        this.adapterGalery = galery;
    }

    @Override
    public void setAdapter(AdapterContainer adapter) {
        adapterContainer = adapter;
    }

    @Override
    public ViewGroup getContainer() {
        return this.containerImg;
    }

    @Override
    public View getInstanceFiller() {
        return new CustomViewImg(getContext());
    }

    @Override
    public void setInstanceCounter(TextView counter) {
        this.counter = counter;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void incrementToCounter() {
        counter.setText(CURRENT_SIZE_CONTAINER() + "/" + MAX_SIZE_CONTAINER());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void decrementToCounter() {
        counter.setText(CURRENT_SIZE_CONTAINER() + "/" + MAX_SIZE_CONTAINER());
    }

    @Override
    public boolean addToContainer(final View view) {
        if (CURRENT_SIZE_CONTAINER() < MAX_SIZE_CONTAINER()) {
            containerImg.addView(view);
            view.setLayoutParams(new LinearLayout.LayoutParams(view.getLayoutParams()));
            incrementToCounter();

            return true;
        }
        return false;
    }

    @Override
    public void removeFromContainer(final View v) {
        this.containerImg.post(() -> {
            containerImg.removeView(v);
            decrementToCounter();
        });
    }

    @Override
    public void clearContainer() {
        this.containerImg.post(() -> {
            containerImg.removeViews(1, CURRENT_SIZE_CONTAINER());
            decrementToCounter();
        });
    }

    @Override
    public int CURRENT_SIZE_CONTAINER() {
        return containerImg.getChildCount() - 1;
    }

    @Override
    public int MAX_SIZE_CONTAINER() {
        return 9;
    }
}