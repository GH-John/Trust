package com.application.arenda.CustomComponents.Panels.BottomNavigation;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.CustomComponents.DrawPanel;

public class PanelBottomNavigation extends Drawable implements DrawPanel {
    private int fillColor, shadowColor;
    private float[] radii;
    private Context context;
    private float shadowRadius, shadowDX, shadowDY;
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PanelBottomNavigation(Context context,
                                 int ResourcesFillColor,
                                 float roundingRadius) {
        float r = roundingRadius;
        this.context = context;
        this.radii = new float[]{r, r, r, r, r, r, r, r};
        setFillColor(ResourcesFillColor);
        setAntiAlians(true);
    }

    public PanelBottomNavigation(Context context,
                                 int ResourcesFillColor,
                                 float upLeftCorner,
                                 float upRightCorner,
                                 float botRightCorner,
                                 float botLeftCorner) {
        this.context = context;
        this.radii = new float[]{upLeftCorner, upLeftCorner, upRightCorner, upRightCorner,
                botRightCorner, botRightCorner, botLeftCorner, botLeftCorner};
        setFillColor(ResourcesFillColor);
        setAntiAlians(true);
    }

    public PanelBottomNavigation(Context context,
                                 int ResourcesFillColor,
                                 int ResourcesShadowColor,
                                 float shadowRadius,
                                 float shadowDX,
                                 float shadowDY,
                                 float roundingRadius) {
        float r = roundingRadius;
        this.context = context;
        this.radii = new float[]{r, r, r, r, r, r, r, r};
        this.shadowRadius = shadowRadius;
        this.shadowDX = shadowDX;
        this.shadowDY = shadowDY;

        setFillColor(ResourcesFillColor);
        setAntiAlians(true);
        setShadowColor(ResourcesShadowColor);
    }

    public PanelBottomNavigation(Context context,
                                 int ResourcesFillColor,
                                 int ResourcesShadowColor,
                                 float shadowRadius,
                                 float shadowDX,
                                 float shadowDY,
                                 float upLeftCorner,
                                 float upRightCorner,
                                 float botRightCorner,
                                 float botLeftCorner) {
        this.context = context;
        this.radii = new float[]{upLeftCorner, upLeftCorner, upRightCorner, upRightCorner,
                botRightCorner, botRightCorner, botLeftCorner, botLeftCorner};
        this.shadowRadius = shadowRadius;
        this.shadowDX = shadowDX;
        this.shadowDY = shadowDY;
        this.fillColor = ResourcesFillColor;
        this.shadowColor = ResourcesShadowColor;

        setFillColor(ResourcesFillColor);
        setAntiAlians(true);
        setShadowColor(ResourcesShadowColor);
    }

    @Override
    public void setFillColor(int resourceColor) {
        paint.setColor(context.getColor(resourceColor));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void setShadowColor(int resourceColor) {
        paint.setShadowLayer(shadowRadius,
                shadowDX, shadowDY,
                context.getColor(resourceColor));
    }

    @Override
    public void setRoundingCorners(float upLeftCorner, float upRightCorner, float botRightCorner, float botLeftCorner) {
        this.radii = new float[]{upLeftCorner, upLeftCorner, upRightCorner, upRightCorner,
                botRightCorner, botRightCorner, botLeftCorner, botLeftCorner};
    }

    @Override
    public void setAntiAlians(boolean b) {
        paint.setAntiAlias(b);
    }

    @Override
    public void setShader(BitmapShader shader) {
        this.paint.setShader(shader);
    }

    @Override
    public void setRoundRect(float[] radii, int width, int height) {
        float PADDING = 25f;
        Point START_END = new Point(width / 2f, height - PADDING);

        Point UP_LEFT = new Point(20f, 0f + PADDING);
        Point CENTER_LEFT = new Point(20f, height / 2f);
        Point DOWN_LEFT = new Point(20f, height - PADDING);
        Point UP_CENTER = new Point(width / 4f, 0f + PADDING);

        Point UP_RIGHT = new Point(width, 0f + PADDING);
        Point CENTER_RIGHT = new Point(width - 60f, height / 2f);
        Point DOWN_RIGHT = new Point(width, height - PADDING);
        Point DOWN_CENTER = new Point(width / 4f, height - PADDING);

        path.reset();
        path.moveTo(START_END.x, START_END.y);
        path.lineTo(DOWN_RIGHT.x, DOWN_RIGHT.y);

        path.cubicTo(
                DOWN_RIGHT.x, DOWN_RIGHT.y,
                CENTER_RIGHT.x, CENTER_RIGHT.y,
                UP_RIGHT.x, UP_RIGHT.y);

        path.lineTo(UP_CENTER.x - UP_LEFT.x, UP_LEFT.y);

        path.cubicTo(
                UP_CENTER.x - UP_LEFT.x, UP_LEFT.y,
                UP_LEFT.x, UP_LEFT.y,
                CENTER_LEFT.x, CENTER_LEFT.y);

        path.cubicTo(
                CENTER_LEFT.x, CENTER_LEFT.y,
                DOWN_LEFT.x, DOWN_LEFT.y,
                DOWN_CENTER.x - DOWN_LEFT.x, DOWN_LEFT.y);

        path.lineTo(START_END.x, START_END.y);
        path.close();
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public Paint getPaint() {
        return this.paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
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

    private class Point {
        float x, y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}