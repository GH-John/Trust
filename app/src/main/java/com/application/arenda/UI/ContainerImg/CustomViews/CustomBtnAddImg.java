package com.application.arenda.UI.ContainerImg.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.arenda.R;

public class CustomBtnAddImg extends ConstraintLayout {

    public CustomBtnAddImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.container_pattern_btn_add_img, this);
    }
}