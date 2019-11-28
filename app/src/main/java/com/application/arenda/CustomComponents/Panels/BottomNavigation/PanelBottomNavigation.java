package com.application.arenda.CustomComponents.Panels.BottomNavigation;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.CustomComponents.DrawPanel;

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
    private Context context;
    private float[] radii;

    private final float BUTTON_CUTOUT_BEGINING = 160.0f;
    private final float BUTTON_CUTOUT_ENDING = 90.0f;
    private final float ROUNDING_ALL_CORNERS = 80.0f;

    PanelBottomNavigation(Context context,
                          int ResourcesFillColor,
                          float[] radii,
                          int ResourcesShadowColor,
                          float shadowRadius,
                          float shadowDX,
                          float shadowDY){
        this.context = context;
        this.radii = radii;
        setDefaultParametrs(ResourcesFillColor, ResourcesShadowColor, shadowRadius, shadowDX, shadowDY);
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void setDefaultParametrs(int ResourcesFillColor,
                                    int ResourcesShadowColor,
                                    float shadowRadius,
                                    float shadowDX,
                                    float shadowDY)
    {
        paint.setColor(context.getColor(ResourcesFillColor));
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius, shadowDX, shadowDY, context.getColor(ResourcesShadowColor));
        cornerPathEffect = new CornerPathEffect(ROUNDING_ALL_CORNERS);
        paint.setPathEffect(cornerPathEffect);
    }

    @Override
    public void setShader(BitmapShader shader) {
        this.paint.setShader(shader);
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public void setRoundRect(float[] radii, int width, int height){
        Point[] arrayPoints = {
                new Point(0f, height),
                new Point(width, height),
                new Point(width, 0f),
                new Point(((width / 2f) + BUTTON_CUTOUT_BEGINING), 0f),
                new Point((width / 2f) + (BUTTON_CUTOUT_ENDING / 2f), ((height / 2f) - 10f)),
                new Point((width / 2f) - (BUTTON_CUTOUT_ENDING / 2f), ((height / 2f) - 10f)),
                new Point(((width / 2f) - BUTTON_CUTOUT_BEGINING), 0f),
                new Point(0f, 0f)
        };

        path.reset();

        path.moveTo(arrayPoints[0].x, arrayPoints[0].y);

        for (int i = 1; i < arrayPoints.length; i++) {
            path.lineTo(arrayPoints[i].x, arrayPoints[i].y);
        }

        path.close();
    }

    @Override
    public Path getPath(){
        return path;
    }

    @Override
    public Paint getPaint() {
        return this.paint;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        setRoundRect(radii, getBounds().width(), getBounds().height());
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