package com.application.arenda.UI;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ComponentBackground extends Drawable implements DrawPanel {
    private int fillColor, shadowColor;
    private float[] radii;
    private Context context;
    private float shadowRadius = 0f, shadowDX = 0f, shadowDY = 0f;
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ComponentBackground(Context context,
                               int ResourcesFillColor,
                               float roundingRadius) {
        float r = roundingRadius;
        this.context = context;
        this.radii = new float[]{r, r, r, r, r, r, r, r};
        setFillColor(ResourcesFillColor);
        setAntiAlians(true);
    }

    public ComponentBackground(Context context,
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

    public ComponentBackground(Context context,
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

    public ComponentBackground(Context context,
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
    public void setShadowParams(float shadowRadius, float shadowDX, float shadowDY){
        this.shadowRadius = shadowRadius;
        this.shadowDX = shadowDX;
        this.shadowDY = shadowDY;
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
        paint.setShader(shader);
    }

    @Override
    public void setRoundRect(float[] radii, int width, int height) {
        if(shadowRadius > 0) {
            path.reset();
            path.addRoundRect(new RectF((shadowRadius * 1.72f) + shadowDX, (shadowRadius * 1.72f) + shadowDY,
                    width - (shadowRadius * 1.72f) + shadowDX, height - (shadowRadius * 1.72f) + shadowDY),
                    radii, Path.Direction.CCW);
            path.close();
        }else {
            path.reset();
            path.addRoundRect(new RectF(0f, 0f, width, height), radii, Path.Direction.CCW);
            path.close();
        }
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
}