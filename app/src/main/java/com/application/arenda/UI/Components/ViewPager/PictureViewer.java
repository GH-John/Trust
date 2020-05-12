package com.application.arenda.UI.Components.ViewPager;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class PictureViewer extends ViewPager {
    private int baseElevation = 0;
    private int raisingElevation = 1;
    private float smallerScale = 0.8f;

    public PictureViewer(@NonNull Context context) {
        super(context);
        transform_viewPager();
    }

    public PictureViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        transform_viewPager();
    }

    private void transform_viewPager() {
        Resources r = getResources();
        int partialWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());

        int viewPagerPadding = partialWidth + pageMargin;
        setPageMargin(pageMargin);
        setPadding(viewPagerPadding, 0, viewPagerPadding, 0);

        setPageTransformer(false, new CardsPagerTransformerBasic(baseElevation, raisingElevation, smallerScale));
    }
}
