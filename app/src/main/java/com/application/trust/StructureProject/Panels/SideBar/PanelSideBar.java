package com.application.trust.StructureProject.Panels.SideBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.trust.StructureProject.Panels.DrawPanel;

public class PanelSideBar extends Drawable implements DrawPanel {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private DisplayMetrics metrics;
    private Context context;

    private final float HEIGHT_PANEL;
    private final float WIDTH_PANEL;

    public PanelSideBar(Context context,
                        int ResourcesFillColor,
                        int ResourcesShadowColor,
                        float shadowRadius,
                        float shadowDX,
                        float shadowDY,
                        float[] radii,
                        int width,
                        int height)
    {
        this.context = context;
        setMetrics(context);
        this.WIDTH_PANEL = width;
        this.HEIGHT_PANEL = height;
        setPaintForPath(ResourcesFillColor, ResourcesShadowColor, shadowRadius, shadowDX, shadowDY);
        setPathPanel(radii);
    }

    @Override
    public void setMetrics(Context context){
        metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if(windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
    }

    @Override
    public DisplayMetrics getMetrics() {
        return metrics;
    }

    @SuppressLint("NewApi")
    @Override
    public void setPaintForPath(int ResourcesFillColor,
                                int ResourcesShadowColor,
                                float shadowRadius,
                                float shadowDX,
                                float shadowDY)
    {
        paint.setColor(context.getColor(ResourcesFillColor));
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius, shadowDX, shadowDY, context.getColor(ResourcesShadowColor));
    }

    @Override
    public void setPathPanel(float[] radii) {
        path.reset();

        path.addRoundRect(new RectF(0, 0, WIDTH_PANEL, HEIGHT_PANEL),
                radii, Path.Direction.CCW);

        path.close();
    }

    @Override
    public Path getPathPanel() {
        return path;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}