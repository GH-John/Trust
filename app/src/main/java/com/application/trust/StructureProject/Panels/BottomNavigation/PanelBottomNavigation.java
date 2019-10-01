package com.application.trust.StructureProject.Panels.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.trust.StructureProject.Panels.DrawPanel;

public class PanelBottomNavigation extends Drawable implements DrawPanel {
    private class Point {
        float x, y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private CornerPathEffect cornerPathEffect;
    private DisplayMetrics metrics;
    private Context context;

    private final float HEIGHT_PANEL = 180.0f;
    private final float BUTTON_CUTOUT_BEGINING = 160.0f;
    private final float BUTTON_CUTOUT_ENDING = 90.0f;
    private final float ROUNDING_ALL_CORNERS = 80.0f;
    private final float HEIGHT_DISPLAY;
    private final float WIDTH_DISPLAY;

    PanelBottomNavigation(Context context, int ResourcesFillColor, float[] radii, int ResourcesShadowColor, float shadowRadius, float shadowDX, float shadowDY){
        this.context = context;
        setMetrics(context);
        this.HEIGHT_DISPLAY = metrics.heightPixels / 12.0f;
        this.WIDTH_DISPLAY = metrics.widthPixels;
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
    public DisplayMetrics getMetrics(){
        return metrics;
    }

    @Override
    @SuppressLint("NewApi")
    public void setPaintForPath(int ResourcesFillColor, int ResourcesShadowColor, float shadowRadius, float shadowDX, float shadowDY){
        paint.setColor(context.getColor(ResourcesFillColor));
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius, shadowDX, shadowDY, context.getColor(ResourcesShadowColor));
        cornerPathEffect = new CornerPathEffect(ROUNDING_ALL_CORNERS);
        paint.setPathEffect(cornerPathEffect);
    }

    @Override
    public void setPathPanel(float[] radii){
        Point[] arrayPoints = {
                new Point(0f, HEIGHT_DISPLAY),
                new Point(WIDTH_DISPLAY, HEIGHT_DISPLAY),
                new Point(WIDTH_DISPLAY, (HEIGHT_DISPLAY - HEIGHT_PANEL)),
                new Point(((WIDTH_DISPLAY / 2f) + BUTTON_CUTOUT_BEGINING), (HEIGHT_DISPLAY - HEIGHT_PANEL)),
                new Point((WIDTH_DISPLAY / 2f) + (BUTTON_CUTOUT_ENDING / 2f), (HEIGHT_DISPLAY - (HEIGHT_PANEL / 2f) - 10f)),
                new Point((WIDTH_DISPLAY / 2f) - (BUTTON_CUTOUT_ENDING / 2f), (HEIGHT_DISPLAY - (HEIGHT_PANEL / 2f) - 10f)),
                new Point(((WIDTH_DISPLAY / 2f) - BUTTON_CUTOUT_BEGINING), (HEIGHT_DISPLAY - HEIGHT_PANEL)),
                new Point(0f, (HEIGHT_DISPLAY - HEIGHT_PANEL)),
                new Point(0f, HEIGHT_DISPLAY)
        };

        path.reset();

        path.moveTo(arrayPoints[0].x, arrayPoints[0].y);

        for (int i = 1; i < arrayPoints.length; i++) {
            path.lineTo(arrayPoints[i].x, arrayPoints[i].y);
        }

        path.close();
    }

    @Override
    public Path getPathPanel(){
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