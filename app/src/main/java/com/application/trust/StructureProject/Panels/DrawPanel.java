package com.application.trust.StructureProject.Panels;

import android.content.Context;
import android.graphics.Path;
import android.util.DisplayMetrics;

public interface DrawPanel {
    void setMetrics(Context context);
    DisplayMetrics getMetrics();
    void setPaintForPath(int ResourcesFillColor, int ResourcesShadowColor, float shadowRadius, float shadowDX, float shadowDY);
    void setPathPanel(float[] radii);
    Path getPathPanel();
}