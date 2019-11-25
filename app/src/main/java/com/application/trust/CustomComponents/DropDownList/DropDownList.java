package com.application.trust.CustomComponents.DropDownList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trust.CustomComponents.FieldStyle.FieldBackground;
import com.application.trust.R;

public class DropDownList extends ConstraintLayout {
    private TextView title;
    private RecyclerView recyclerView;
    private ImageView background, icon;

    private RecyclerView.Adapter adapter;
    public boolean isHiden = true, isExpand = false;

    public DropDownList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.drop_down_list, this);
        icon = findViewById(R.id.iconDDL);
        title = findViewById(R.id.titleDDL);
        background = findViewById(R.id.backgroundDDL);
        recyclerView = findViewById(R.id.recyclerViewDDL);
    }

    private void initializationStyles() {
        background.setImageDrawable(new FieldBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f}));
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setIcon(Drawable icon){
        this.icon.setImageDrawable(icon);
    }

    public void setBackground(Drawable background) {
        this.background.setImageDrawable(background);
    }

    public void setAdapterRecyclerView(RecyclerView.Adapter adapter){
        this.adapter = adapter;
    }

    public void hideList(){
        isHiden = true;
        isExpand = false;
        recyclerView.removeAllViews();
        this.requestLayout();
    }

    public void expandList(){
        if(adapter != null) {
            isHiden = false;
            isExpand = true;

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            this.requestLayout();
        }
    }

    public void rotateIcon(float angle){
        icon.setRotation(angle);
    }
}