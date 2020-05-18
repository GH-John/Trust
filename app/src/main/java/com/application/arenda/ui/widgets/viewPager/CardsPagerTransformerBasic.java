package com.application.arenda.ui.widgets.viewPager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

class CardsPagerTransformerBasic implements ViewPager.PageTransformer {
    private int baseElevation;
    private int raisingElevation;
    private float smallerScale;

    public CardsPagerTransformerBasic(int baseElevation, int raisingElevation, float smallerScale) {
        this.baseElevation = baseElevation;
        this.raisingElevation = raisingElevation;
        this.smallerScale = smallerScale;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        float absPosition = Math.abs(position);

        if (absPosition >= 1) {
            page.setElevation(baseElevation);
            page.setScaleY(smallerScale);
        } else {
            page.setElevation(((1 - absPosition) * raisingElevation + baseElevation));
            page.setScaleY((smallerScale - 1) * absPosition + 1);
        }
    }
}
