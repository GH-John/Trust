package com.application.trust.CustomComponents.FieldStyle;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

import com.application.trust.CustomComponents.DrawPanel;

public class SetFieldStyle {
    public static void setEditTextBackground(DrawPanel style, EditText ... fields){
        if(fields.length > 0){
            for (EditText field: fields) {
                field.setBackground((Drawable) style);
            }
        }
    }
}