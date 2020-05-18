package com.application.arenda.ui;

import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Path;

public interface DrawPanel {
    void setShader(BitmapShader shader);

    void setRoundRect(float[] radii, int width, int height);

    void setAntiAlians(boolean b);

    void setFillColor(int resourceColor);

    void setShadowParams(float shadowRadius, float shadowDX, float shadowDY);

    void setShadowColor(int resourceColor);

    void setRoundingCorners(float upLeftCorner,
                            float upRightCorner,
                            float botRightCorner,
                            float botLeftCorner);

    Path getPath();

    void setPath(Path path);

    Paint getPaint();

    void setPaint(Paint paint);
}