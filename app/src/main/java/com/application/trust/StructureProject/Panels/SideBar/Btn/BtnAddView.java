package com.application.trust.StructureProject.Panels.SideBar.Btn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.trust.R;
import com.application.trust.StructureProject.Panels.DrawPanel;


public class BtnAddView extends ConstraintLayout implements IBtnAdd{
    private ImageView panelBtnAdd;

    public BtnAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateBtnAdd(context, attrs);
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
                new float[]{0f,0f, 0f,0f, 80f,80f, 0f,0f},
                this.panelBtnAdd.getMeasuredWidth(), this.panelBtnAdd.getMeasuredHeight());
        panelBtnAdd.setGradientForPath
                (0,0,600,100, R.color.colorPink, R.color.colorBlue);
        stylePanelBtnAdd(getContext(), panelBtnAdd);
    }

    @Override
    public void stylePanelBtnAdd(Context context, DrawPanel drawPanel) {
        panelBtnAdd.setImageDrawable((Drawable)drawPanel);
    }

    @Override
    public void styleItemBtnAdd(Context context) {

    }

    @Override
    public void startListenerBtnAdd(final AppCompatActivity activity, int idContainerContent) {
        panelBtnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"add click", Toast.LENGTH_LONG).show();
            }
        });
    }
}