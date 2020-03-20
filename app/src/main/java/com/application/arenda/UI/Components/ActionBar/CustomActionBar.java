package com.application.arenda.UI.Components.ActionBar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;

public class CustomActionBar extends CardView implements ComponentManager.Observer {

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.ab_container, this);
    }

    @Override
    public void update(@NonNull Object object) {
        if (object instanceof AdapterActionBar) {
            setVisibility(VISIBLE);
            this.removeAllViewsInLayout();
            inflate(getContext(), ((AdapterActionBar) object).getIdPatternResource(),
                    this);
            ((AdapterActionBar) object).initComponentsActionBar(this);
            ((AdapterActionBar) object).initListenersActionBar(this);
        } else {
            setVisibility(INVISIBLE);
        }
    }
}