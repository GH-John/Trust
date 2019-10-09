package com.application.trust.CustomComponents.Panels.ActionBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.Patterns.Observer;
import com.application.trust.R;

public class CustomActionBar extends ConstraintLayout implements IActionBar, Observer {
    ImageView panelActionBar,
            itemBurgerMenu;

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateActionBar(context, attrs);
        stylePanel(context, new PanelActionBar(context,
                R.color.colorWhite, new float[]{0f, 0f, 0f, 0f, 80f, 80f, 80f, 80f},
                R.color.shadowColor, 10f, 0f, 0f));
        startListener(R.id.containerContentDisplay);
    }

    private void inflateActionBar(Context context, AttributeSet attrs) {
        inflate(context, R.layout.action_bar, this);
        panelActionBar = findViewById(R.id.panelActionBar);
        itemBurgerMenu = findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void stylePanel(Context context, DrawPanel drawPanel) {
        this.panelActionBar.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void styleItem(Context context) {

    }

    @Override
    public void startListener(int idContainerContent) {

    }

    @Override
    public void update() {

    }
}