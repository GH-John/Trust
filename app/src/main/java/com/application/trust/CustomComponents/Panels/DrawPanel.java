package com.application.trust.CustomComponents.Panels;

import android.graphics.Path;

public interface DrawPanel {
    void setPaint(int ResourcesFillColor, int ResourcesShadowColor, float shadowRadius, float shadowDX, float shadowDY);
    void setPath(float[] radii, int width, int height);
    Path getPath();
}