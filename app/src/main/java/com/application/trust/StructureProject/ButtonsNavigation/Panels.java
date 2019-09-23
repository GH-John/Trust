package com.application.trust.StructureProject.ButtonsNavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class Point {
    float x, y;

    Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

public class Panels extends Drawable {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private Context context;
    private CornerPathEffect cornerPathEffect;
    private float roundingCorners = 100f;

    @SuppressLint("NewApi")
    Panels(Context context, int color, Paint.Style style){
        this.context = context;
        paint.setColor(context.getColor(color));
        paint.setStyle(style);
    }

    private void createPointsPanels(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if(windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }

        float height = metrics.heightPixels / 12f;
        float width = metrics.widthPixels;

        float heightPanel = 185f;

        float pointButtonRounding1 = 110f;
        float pointButtonRounding2 = 110f;

        Point[] arrayPoints = {
                new Point(0f, height),
                new Point(width, height),
                new Point(width, (height - heightPanel)),
                new Point(((width / 2f) + pointButtonRounding1), (height - heightPanel)),
                new Point((width / 2f) + (pointButtonRounding1 / 2f), (height - (heightPanel / 2f))),
                new Point((width / 2f) - (pointButtonRounding2 / 2f), (height - (heightPanel / 2f))),
                new Point(((width / 2f) - pointButtonRounding2), (height - heightPanel)),
                new Point(0f, (height - heightPanel)),
                new Point(0f, height)
        };

        path.reset();

        path.moveTo(arrayPoints[0].x, arrayPoints[0].y);

        for (int i = 1; i < arrayPoints.length; i++) {
            path.lineTo(arrayPoints[i].x, arrayPoints[i].y);
        }

        path.close();
        cornerPathEffect = new CornerPathEffect(roundingCorners);
        paint.setPathEffect(cornerPathEffect);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        createPointsPanels();
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