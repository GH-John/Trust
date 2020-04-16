package com.application.arenda.UI.ContainerImg.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CustomViewImg extends ConstraintLayout {
    @Nullable
    @BindView(R.id.itemImg)
    ImageView itemImg;

    @Nullable
    @BindView(R.id.itemDeleteSelectedImg)
    ImageView itemDeleteSelectedImg;

    @Nullable
    @BindView(R.id.imgRadioSelecter)
    RadioButton radioButton;

    public CustomViewImg(Context context) {
        super(context);
        initComponents();
    }

    public CustomViewImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponents();
    }

    private void initComponents() {
        inflate(getContext(), R.layout.container_pattern_selected_img, this);
        ButterKnife.bind(this);
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        try {
            if (bitmap != null)
                itemImg.setImageBitmap(bitmap);
            return true;
        } catch (IllegalArgumentException e) {
            Timber.e(e);
            Utils.messageOutput(getContext(), getContext().getString(R.string.error_please_choose_correct_img));
        }

        return false;
    }

    public ImageView getItemImg() {
        return itemImg;
    }

    public void setItemChecked(boolean b) {
        radioButton.setChecked(b);
    }

    public void setImgClickListener(OnClickListener listener) {
        itemImg.setOnClickListener(listener);
    }

    public void itemDeleteOnClickListener(OnClickListener onClickListener) {
        itemDeleteSelectedImg.setOnClickListener(onClickListener);
    }
}