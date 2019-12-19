package com.application.arenda.UI.Panels.ActionBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.arenda.UI.ContainerFragments.AdapterLink;
import com.application.arenda.UI.ContainerFragments.ComponentLinkManager;
import com.application.arenda.UI.DrawPanel;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.R;

public class CustomActionBar extends ConstraintLayout implements ActionBar, AdapterLink, Observer {
    private ImageView panelActionBar;
    private ViewGroup patternContainer;

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateActionBar(context, attrs);
        stylePanel(context, new PanelActionBar(context,
                R.color.colorWhite, R.color.shadowColor, 10f, 0f, 0f, 0f, 0f, 80f, 80f));
    }

    private void inflateActionBar(Context context, AttributeSet attrs) {
        inflate(context, R.layout.ab_container, this);
        patternContainer = findViewById(R.id.patternContainer);
        panelActionBar = findViewById(R.id.panelActionBar);
    }

    @Override
    public void stylePanel(Context context, DrawPanel drawPanel) {
        this.panelActionBar.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void update(@NonNull Object object) {
        if(object instanceof AdapterActionBar) {
            patternContainer.removeAllViewsInLayout();
            inflate(getContext(), ((AdapterActionBar) object).getIdPatternResource(),
                    patternContainer);
            ((AdapterActionBar) object).initializationComponentsActionBar(patternContainer);
            ((AdapterActionBar) object).initializationListenersActionBar(patternContainer);
        }
    }

    @Override
    public void createLinkWithFragment(ComponentLinkManager componentLinkManager) {

    }
}