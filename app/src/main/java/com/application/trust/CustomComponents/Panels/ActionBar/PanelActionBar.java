package com.application.trust.CustomComponents.Panels.ActionBar;

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

public class PanelActionBar extends Drawable implements DrawPanel {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private Context context;
    private float[] radii;

    public PanelActionBar(Context context,
                          int ResourcesFillColor,
                          float[] radii,
                          int ResourcesShadowColor,
                          float shadowRadius,
                          float shadowDX,
                          float shadowDY)
    {
        this.context = context;
        this.radii = radii;
        setPaint(ResourcesFillColor, ResourcesShadowColor, shadowRadius, shadowDX, shadowDY);
    }

    @Override
    @SuppressLint("NewApi")
    public void setPaint(int ResourcesFillColor,
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
    public void setPath(float[] radii, int width, int height){
        path.reset();

        path.addRoundRect(new RectF(0, 0, width, height), radii, Path.Direction.CCW);

        path.close();
    }

    @Override
    public Path getPath(){
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