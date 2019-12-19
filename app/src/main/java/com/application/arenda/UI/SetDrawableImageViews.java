package com.application.arenda.UI;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SetDrawableImageViews {
    public static void setStyle(DrawPanel style, ImageView... imageViews) {
        if (imageViews.length > 0) {
            for (ImageView imageView : imageViews) {
                imageView.setBackground((Drawable) style);
            }
        }
    }
}