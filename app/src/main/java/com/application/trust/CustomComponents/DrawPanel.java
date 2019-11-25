package com.application.trust.CustomComponents;

import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Path;

public interface DrawPanel {
    void setPaint(Paint paint);
    void setDefaultParametrs(int ResourcesFillColor, int ResourcesShadowColor, float shadowRadius, float shadowDX, float shadowDY);
    void setShader(BitmapShader shader);
    void setPath(Path path);
    void setRoundRect(float[] radii, int width, int height);
    Path getPath();
    Paint getPaint();
}