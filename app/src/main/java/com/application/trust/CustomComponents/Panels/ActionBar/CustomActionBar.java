package com.application.trust.CustomComponents.Panels.ActionBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Container.FragmentLink;
import com.application.trust.CustomComponents.Container.ManagerFragmentLinks;
import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.Patterns.Observer;
import com.application.trust.R;

public class CustomActionBar extends ConstraintLayout implements IActionBar, FragmentLink, Observer {
    private ImageView panelActionBar;
    private LayoutInflater inflater;

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateActionBar(context, attrs);
        stylePanel(context, new PanelActionBar(context,
                R.color.colorWhite, new float[]{0f, 0f, 0f, 0f, 80f, 80f, 80f, 80f},
                R.color.shadowColor, 10f, 0f, 0f));
    }

    private void inflateActionBar(Context context, AttributeSet attrs) {
        inflate(context, R.layout.action_bar_container, this);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        panelActionBar = findViewById(R.id.panelActionBar);
    }

    @Override
    public void stylePanel(Context context, DrawPanel drawPanel) {
        this.panelActionBar.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void update(Fragment fragment) {
        if(fragment instanceof AdapterActionBar) {
            inflater.inflate(((AdapterActionBar)fragment).getIdPatternResource(), this);
            ((AdapterActionBar)fragment).initializeItems(this);
            ((AdapterActionBar)fragment).initializeItemsListener(this);
        }
    }

    @Override
    public void createItemLinks(ManagerFragmentLinks managerFragmentLinks) {

    }
}