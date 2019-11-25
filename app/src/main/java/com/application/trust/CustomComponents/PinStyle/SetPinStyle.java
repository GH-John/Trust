package com.application.trust.CustomComponents.PinStyle;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.application.trust.CustomComponents.DrawPanel;

public class SetPinStyle {
    public static void setStyle(DrawPanel style, ImageView... imageViews){
        if(imageViews.length > 0){
            for (ImageView imageView: imageViews) {
                imageView.setBackground((Drawable) style);
            }
        }
    }
}