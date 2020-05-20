package com.application.arenda.ui.widgets.viewPager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

class CardsPagerTransformerBasic implements ViewPager.PageTransformer {
    private float smallerScale;

    public CardsPagerTransformerBasic(float smallerScale) {
        this.smallerScale = smallerScale;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        float absPosition = Math.abs(position);

        if (absPosition >= 1) {
            page.setScaleY(smallerScale);
        } else {
            page.setScaleY((smallerScale - 1) * absPosition + 1);
        }
    }
}
