package com.application.trust.StructureProject.Panels.ActionBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.trust.R;
import com.application.trust.StructureProject.Panels.DrawPanel;

public class ActionBarView extends ConstraintLayout implements IActionBar{
    ImageView panelActionBar,
              itemBurgerMenu;

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateActionBar(context, attrs);
        stylePanelActionBar
                (context, new PanelActionBar(context,
                        R.color.colorWhite, new float[8], R.color.shadowColor, 10f,0f,0f));
    }
    private void inflateActionBar(Context context, AttributeSet attrs){
        inflate(context, R.layout.action_bar, this);
        panelActionBar = findViewById(R.id.panelActionBar);
        itemBurgerMenu = findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void stylePanelActionBar(Context context, DrawPanel drawPanel) {
        this.panelActionBar.setImageDrawable((Drawable)drawPanel);
    }

    @Override
    public void styleItemActionBar(Context context) {

    }

    @Override
    public void startListenerActionBar(AppCompatActivity activity, int idContainerContent) {

    }
}