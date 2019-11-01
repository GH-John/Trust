package com.application.trust.CustomComponents.FieldStyle;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

import com.application.trust.CustomComponents.Panels.DrawPanel;

public class SetFieldStyle {
    public static void setStyle(DrawPanel style, EditText ... fields){
        if(fields.length > 0){
            for (EditText field: fields) {
                field.setBackground((Drawable) style);
            }
        }
    }
}