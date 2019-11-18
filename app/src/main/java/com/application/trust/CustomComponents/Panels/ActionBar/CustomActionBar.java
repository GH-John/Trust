package com.application.trust.CustomComponents.Panels.ActionBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.ContainerFragments.FragmentLink;
import com.application.trust.CustomComponents.ContainerFragments.ComponentLinkManager;
import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.Patterns.Observer;
import com.application.trust.R;

public class CustomActionBar extends ConstraintLayout implements ActionBar, FragmentLink, Observer {
    private ImageView panelActionBar;
    private ViewGroup patternContainer;

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateActionBar(context, attrs);
        stylePanel(context, new PanelActionBar(context,
                R.color.colorWhite, new float[]{0f, 0f, 0f, 0f, 80f, 80f, 80f, 80f},
                R.color.shadowColor, 10f, 0f, 0f));
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
    public void update(Fragment fragment) {
        if(fragment instanceof AdapterActionBar) {
            patternContainer.removeAllViewsInLayout();
            inflate(getContext(), ((AdapterActionBar)fragment).getIdPatternResource(),
                    patternContainer);
            ((AdapterActionBar)fragment).initializationComponentsActionBar(patternContainer);
            ((AdapterActionBar)fragment).initializationListenersActionBar(patternContainer);
        }
    }

    @Override
    public void createLinkWithFragment(ComponentLinkManager componentLinkManager) {

    }
}