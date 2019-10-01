package com.application.trust.StructureProject.Panels.SideBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.trust.R;
import com.application.trust.StructureProject.Panels.DrawPanel;

public class SideBarView extends ConstraintLayout implements ISideBar{
    ImageView panelSideBar,
              itemUserAccount,
              backgroundItemList;


    public SideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateSideBar(context, attrs);
        stylePanelSideBar(context, new PanelSideBar(context, R.color.colorWhite, new float[8],
                R.color.shadowColor, 10f, 0f, 0f));
        styleItemSideBar(context);
    }

    private void inflateSideBar(Context context, AttributeSet attrs){
        inflate(context, R.layout.side_bar, this);
        panelSideBar = findViewById(R.id.panelSideBar);
        itemUserAccount = findViewById(R.id.itemUserAccount);
        backgroundItemList = findViewById(R.id.backgroundItemList);
    }

    @Override
    public void stylePanelSideBar(Context context, DrawPanel drawPanel) {
        panelSideBar.setImageDrawable((Drawable) drawPanel);
    }

    @SuppressLint("NewApi")
    @Override
    public void styleItemSideBar(Context context) {
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0,0, view.getWidth(), view.getHeight(), 100);
            }
        };

        itemUserAccount.setOutlineProvider(viewOutlineProvider);
        itemUserAccount.setClipToOutline(true);
    }

    @Override
    public void startListenerSideBar(AppCompatActivity activity, int idContainerContent) {

    }
}