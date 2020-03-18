package com.application.arenda.UI.Style;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

import com.application.arenda.UI.DrawPanel;

public class SetFieldStyle {
    public static void setEditTextBackground(DrawPanel style, EditText ... fields){
        if(fields.length > 0){
            for (EditText field: fields) {
                field.setBackground((Drawable) style);
            }
        }
    }
}