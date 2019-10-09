package com.application.trust.CustomComponents.Panels.SideBar.Btn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.trust.CustomComponents.Panels.DrawPanel;

public class PanelBtnAdd extends Drawable implements DrawPanel {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private DisplayMetrics metrics;
    private Context context;
    private float[] radii;

    public PanelBtnAdd(Context context,
                       int ResourcesFillColor,
                       int ResourcesShadowColor,
                       float shadowRadius,
                       float shadowDX,
                       float shadowDY,
                       float[] radii)
    {
        this.context = context;
        this.radii = radii;
        setPaint(ResourcesFillColor, ResourcesShadowColor, shadowRadius, shadowDX, shadowDY);
    }

    @SuppressLint({"NewApi"})
    @Override
    public void setPaint(int ResourcesFillColor,
                                int ResourcesShadowColor,
                                float shadowRadius,
                                float shadowDX,
                                float shadowDY)
    {
        paint.setColor(ResourcesFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius, shadowDX, shadowDY, context.getColor(ResourcesShadowColor));
    }

    @SuppressLint("NewApi")
    public void setGradient(float x0,
                                   float y0,
                                   float x1,
                                   float y1,
                                   int firstColorResource,
                                   int secondColorResource)
    {
        paint.setShader(new LinearGradient(x0, y0, x1, y1,
                context.getColor(firstColorResource), context.getColor(secondColorResource), Shader.TileMode.CLAMP));
    }

    @Override
    public void setPath(float[] radii, int width, int height) {
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