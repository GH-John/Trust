package com.application.trust.CustomComponents.Panels.SideBar;

import android.annotation.SuppressLint;
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

import com.application.trust.CustomComponents.Panels.DrawPanel;

public class PanelSideBar extends Drawable implements DrawPanel {

    private float[] radii;
    private Context context;
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PanelSideBar(Context context,
                        int ResourcesFillColor,
                        int ResourcesShadowColor,
                        float shadowRadius,
                        float shadowDX,
                        float shadowDY,
                        float[] radii) {
        this.context = context;
        this.radii = radii;
        setDefaultParametrs(ResourcesFillColor, ResourcesShadowColor,
                shadowRadius,
                shadowDX, shadowDY);
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @SuppressLint("NewApi")
    @Override
    public void setDefaultParametrs(int ResourcesFillColor,
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
    public void setShader(BitmapShader shader) {
        this.paint.setShader(shader);
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public void setRoundRect(float[] radii, int width, int height) {
        path.reset();

        path.addRoundRect(new RectF(0, 0, width, height), radii, Path.Direction.CCW);

        path.close();
    }

    @Override
    public Path getPath() {
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