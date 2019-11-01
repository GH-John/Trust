package com.application.trust.CustomComponents.PinStyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.trust.CustomComponents.Panels.DrawPanel;

public class PinBackground extends Drawable implements DrawPanel {
    private float[] radii;
    private Context context;
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PinBackground(Context context,
                           int ResourcesFillColor,
                           int ResourcesShadowColor,
                           float shadowRadius,
                           float shadowDX,
                           float shadowDY,
                           float[] radii) {
        this.context = context;
        this.radii = radii;
        setPaint(ResourcesFillColor, ResourcesShadowColor,
                shadowRadius,
                shadowDX, shadowDY);
    }

    @SuppressLint("NewApi")
    @Override
    public void setPaint(int ResourcesFillColor,
                         int ResourcesShadowColor,
                         float shadowRadius,
                         float shadowDX,
                         float shadowDY) {
        paint.setColor(context.getColor(ResourcesFillColor));
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius,
                shadowDX, shadowDY,
                context.getColor(ResourcesShadowColor));
    }

    @Override
    public void setPath(float[] radii, int width, int height) {
        path.reset();

        path.addRoundRect(new RectF(0, 10, width - 10, height - 10), radii, Path.Direction.CCW);

        path.close();
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        setPath(radii, getBounds().width(), getBounds().height());
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