package com.application.trust.StructureProject.Panels.ActionBar;

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

public class PanelActionBar extends Drawable {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private DisplayMetrics metrics;
    private Context context;

    private final float HEIGHT_PANEL = 18.0f;
    private final float ROUNDING_BOTTOM_LEFT_CORNER = 80.0f;
    private final float ROUNDING_BOTTOM_RIGHT_CORNER = 80.0f;
    private final float HEIGHT_DISPLAY;
    private final float WIDTH_DISPLAY;

    public PanelActionBar(Context context, int ResourcesFillColor, int ResourcesShadowColor, float shadowRadius, float shadowDX, float shadowDY){
        this.context = context;
        getMetrics(context);
        this.HEIGHT_DISPLAY = metrics.heightPixels / 12f;
        this.WIDTH_DISPLAY = metrics.widthPixels;
        setPaintForPath(ResourcesFillColor, ResourcesShadowColor, shadowRadius, shadowDX, shadowDY);
        setPathPanels();
    }

    private void getMetrics(Context context){
        metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if(windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
    }

    @SuppressLint("NewApi")
    private void setPaintForPath(int ResourcesFillColor,
                                 int ResourcesShadowColor,
                                 float shadowRadius,
                                 float shadowDX,
                                 float shadowDY)
    {
        paint.setColor(context.getColor(ResourcesFillColor));
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius, shadowDX, shadowDY, context.getColor(ResourcesShadowColor));
    }

    private void setPathPanels(){
        path.reset();

        float[] corners = new float[]{
                0, 0,        // Top left radius in px
                0, 0,        // Top right radius in px
                ROUNDING_BOTTOM_RIGHT_CORNER, ROUNDING_BOTTOM_RIGHT_CORNER,
                ROUNDING_BOTTOM_LEFT_CORNER, ROUNDING_BOTTOM_LEFT_CORNER
        };
        path.addRoundRect(new RectF(0, 0, WIDTH_DISPLAY, HEIGHT_DISPLAY - HEIGHT_PANEL), corners, Path.Direction.CCW);

        path.close();
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