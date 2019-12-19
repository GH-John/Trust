package com.application.arenda.UI.ContainerImg.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.R;

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
        inflate(getContext(), R.layout.container_pattern_selected_img, this);
        itemImg = findViewById(R.id.itemImg);
        itemDeleteSelectedImg = findViewById(R.id.itemDeleteSelectedImg);
    }

    private void initializationStyles() {
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        try {
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            ComponentBackground background = new ComponentBackground(getContext(), R.color.colorWhite,
                    R.color.shadowColor, 6f, 0f, 3f, 20f);
            background.setShader(shader);

            itemImg.setImageDrawable(background);
            return true;
        } catch (IllegalArgumentException ignored) {
            Toast.makeText(getContext(), R.string.error_please_choose_correct_img, Toast.LENGTH_LONG).show();
            Log.d("Bitmap", "attempt to assign bitmap invalid data");
            return false;
        }
    }

    public ImageView getItemImg() {
        return itemImg;
    }


    public void itemDeleteOnClickListener(OnClickListener onClickListener) {
        itemDeleteSelectedImg.setOnClickListener(onClickListener);
    }
}