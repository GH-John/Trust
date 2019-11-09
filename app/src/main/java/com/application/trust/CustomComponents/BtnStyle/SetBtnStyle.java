package com.application.trust.CustomComponents.BtnStyle;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import com.application.trust.CustomComponents.Panels.DrawPanel;

public class SetBtnStyle {
    public static void setStyle(DrawPanel style, Button... buttons){
        if(buttons.length > 0){
            for (Button button: buttons) {
                button.setBackground((Drawable) style);
            }
        }
    }
}