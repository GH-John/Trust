package com.application.arenda.UI.ContainerImg.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.arenda.R;

public class CustomViewImg extends ConstraintLayout {
    private ImageView itemImg, itemDeleteSelectedImg;

    public CustomViewImg(Context context) {
        super(context);
        initializationComponents();
    }

    public CustomViewImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.container_pattern_selected_img, this);
        itemImg = findViewById(R.id.itemImg);
        itemDeleteSelectedImg = findViewById(R.id.itemDeleteSelectedImg);
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        try {
            itemImg.setImageBitmap(bitmap);
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