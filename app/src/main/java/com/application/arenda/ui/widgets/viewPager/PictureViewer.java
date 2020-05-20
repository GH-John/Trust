package com.application.arenda.ui.widgets.viewPager;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class PictureViewer extends ViewPager {
    private float smallerScale = 0.9f;

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
        int partialWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        int viewPagerPadding = partialWidth + pageMargin + 10;
        setPageMargin(pageMargin);
        setPadding(viewPagerPadding, viewPagerPadding / 2, viewPagerPadding, viewPagerPadding / 2);

        setPageTransformer(false, new CardsPagerTransformerBasic(smallerScale));
    }
}
