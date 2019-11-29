package com.application.arenda.CustomComponents.ContainerImg.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.arenda.CustomComponents.BtnStyle.BtnBackground;
import com.application.arenda.R;

public class CustomBtnAddImg extends ConstraintLayout {
    private ImageView backgroundBtnAdd;

    public CustomBtnAddImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.container_pattern_btn_add_img, this);
        backgroundBtnAdd = findViewById(R.id.backgroundBtnAdd);
    }

    private void initializationStyles() {
        backgroundBtnAdd.setImageDrawable(new BtnBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f}));
    }
}