package com.application.trust.CustomComponents.ContainerImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.trust.CustomComponents.BtnStyle.BtnBackground;
import com.application.trust.R;

public class CustomViewImg extends ConstraintLayout {
    private ImageView itemImg, itemDeleteSelectedImg;

    public CustomViewImg(Context context) {
        super(context);
        initializationComponents();
        initializationStyles();
    }

    public CustomViewImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.view_selected_img, this);
        itemImg = findViewById(R.id.itemImg);
        itemDeleteSelectedImg = findViewById(R.id.itemDeleteSelectedImg);
    }

    private void initializationStyles() {
    }

    public void setImageBitmap(Bitmap bitmap){
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        BtnBackground background = new BtnBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor,  6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});
        background.setShader(shader);

        itemImg.setImageDrawable(background);
    }

    public ImageView getItemImg() {
        return itemImg;
    }


    public void itemDeleteOnClickListener(OnClickListener onClickListener) {
        itemDeleteSelectedImg.setOnClickListener(onClickListener);
    }
}