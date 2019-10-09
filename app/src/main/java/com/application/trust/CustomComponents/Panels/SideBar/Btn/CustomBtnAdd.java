package com.application.trust.CustomComponents.Panels.SideBar.Btn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.R;


public class CustomBtnAdd extends ConstraintLayout implements IBtnAdd{
    private ImageView panelBtnAdd;

    public CustomBtnAdd(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateBtnAdd(context, attrs);
        startListener(R.id.containerContentDisplay);
    }

    private void inflateBtnAdd(Context context, AttributeSet attrs){
        inflate(context, R.layout.btn_add, this);
        panelBtnAdd = findViewById(R.id.panelBtnAdd);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        PanelBtnAdd panelBtnAdd = new PanelBtnAdd(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f,0f,3.0f,
                new float[]{0f,0f, 0f,0f, 80f,80f, 0f,0f});
        panelBtnAdd.setGradient
                (0,0,600,100, R.color.colorPink, R.color.colorBlue);
        stylePanel(getContext(), panelBtnAdd);
    }

    @Override
    public void stylePanel(Context context, DrawPanel drawPanel) {
        panelBtnAdd.setImageDrawable((Drawable)drawPanel);
    }

    @Override
    public void styleItem(Context context) {

    }

    @Override
    public void startListener(int idContainerContent) {
        panelBtnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"add click", Toast.LENGTH_LONG).show();
            }
        });
    }
}