package com.application.arenda.CustomComponents.DropDownList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.CustomComponents.FieldStyle.FieldBackground;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories.AdapterDropDownList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DropDownList extends ConstraintLayout implements IDropDownList, Observer {
    public boolean isHiden = true, isExpand = false, isSetAdapter = false;
    private TextView title;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ImageView background, icon;
    private RecyclerView.Adapter adapter;

    private List list;

    public DropDownList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.drop_down_list, this);
        list = new ArrayList();
        icon = findViewById(R.id.iconDDL);
        title = findViewById(R.id.titleDDL);
        background = findViewById(R.id.backgroundDDL);
        progressBar = findViewById(R.id.progressBarDDL);
        recyclerView = findViewById(R.id.recyclerViewDDL);
    }

    private void initializationStyles() {
        background.setImageDrawable(new FieldBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f}));
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setIcon(Drawable icon) {
        this.icon.setImageDrawable(icon);
    }

    public void setBackground(Drawable background) {
        this.background.setImageDrawable(background);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        ((AdapterDropDownList) adapter).setDropDownList(this);
    }

    @Override
    public void hideList() {
        if (adapter != null) {
            isHiden = true;
            isExpand = false;
            rotateIcon(0f);

            list.clear();
            list.addAll(((AdapterDropDownList) adapter).getCollection());
            ((AdapterDropDownList) adapter).clearRecyclerView();
        }
    }

    @Override
    public void expandList() {
        if (adapter != null) {
            isHiden = false;
            isExpand = true;
            rotateIcon(90f);

            if (!isSetAdapter) {
                isSetAdapter = true;

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                refreshList(list);
            }
        }
    }

    @Override
    public View getProgressBar() {
        return progressBar;
    }

    @Override
    public void refreshList(Collection collection) {
        ((AdapterDropDownList) adapter).refreshCollection(collection);
    }

    public void rotateIcon(float angle) {
        icon.setRotation(angle);
    }

    @Override
    public void update(Object object) {
        if (object instanceof Collection)
            refreshList((Collection) object);
    }
}