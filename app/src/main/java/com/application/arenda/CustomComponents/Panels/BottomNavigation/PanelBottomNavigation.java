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
    private final float BUTTON_CUTOUT_BEGINING = 160.0f;
    private final float BUTTON_CUTOUT_ENDING = 90.0f;
    private final float ROUNDING_ALL_CORNERS = 80.0f;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private CornerPathEffect cornerPathEffect;

    private Context context;
    private float[] radii;

    PanelBottomNavigation(Context context,
                          int ResourcesFillColor,
                          float[] radii,
                          int ResourcesShadowColor,
                          float shadowRadius,
                          float shadowDX,
                          float shadowDY) {
        this.context = context;
        this.radii = radii;
        setDefaultParametrs(ResourcesFillColor, ResourcesShadowColor, shadowRadius, shadowDX, shadowDY);
    }

    @Override
    public void setDefaultParametrs(int ResourcesFillColor,
                                    int ResourcesShadowColor,
                                    float shadowRadius,
                                    float shadowDX,
                                    float shadowDY) {
        paint.setColor(context.getColor(ResourcesFillColor));
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius, shadowDX, shadowDY, context.getColor(ResourcesShadowColor));
//        cornerPathEffect = new CornerPathEffect(ROUNDING_ALL_CORNERS);
//        paint.setPathEffect(cornerPathEffect);
    }

    @Override
    public void setShader(BitmapShader shader) {
        this.paint.setShader(shader);
    }

    @Override
    public void setRoundRect(float[] radii, int width, int height) {
//        Point[] arrayPoints = {
//                new Point(0f, height),
//                new Point(width, height),
//                new Point(width, 0f),
//                new Point(((width / 2f) + BUTTON_CUTOUT_BEGINING), 0f),
//                new Point((width / 2f) + (BUTTON_CUTOUT_ENDING / 2f), ((height / 2f) - 10f)),
//                new Point((width / 2f) - (BUTTON_CUTOUT_ENDING / 2f), ((height / 2f) - 10f)),
//                new Point(((width / 2f) - BUTTON_CUTOUT_BEGINING), 0f),
//                new Point(0f, 0f)
//        };
//
//        path.reset();
//
//        path.moveTo(arrayPoints[0].x, arrayPoints[0].y);
//
//        for (int i = 1; i < arrayPoints.length; i++) {
//            path.lineTo(arrayPoints[i].x, arrayPoints[i].y);
//        }

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