package com.application.arenda.CustomComponents.ContainerImg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.arenda.CustomComponents.ContainerImg.CustomViews.CustomBtnAddImg;
import com.application.arenda.CustomComponents.ContainerImg.CustomViews.CustomViewImg;
import com.application.arenda.CustomComponents.ContainerImg.Galery.AdapterGalery;
import com.application.arenda.CustomComponents.ContainerImg.Galery.ChooseImages;
import com.application.arenda.R;

public class ContainerSelectedImages extends LinearLayout implements Container {
    private LinearLayout containerImg;
    private CustomBtnAddImg btnAddImg;
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
        btnAddImg = findViewById(R.id.btnAddImg);
        containerImg = findViewById(R.id.containerImg);
    }

    private void initializationStyles() {
    }

    private void initializationListeners() {
        btnAddImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooseImages(adapterGalery).open();
            }
        });
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
        this.containerImg.post(new Runnable() {
            public void run() {
                containerImg.removeView(v);
                decrementToCounter();
            }
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