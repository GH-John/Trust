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
import java.util.Stack;

public class DropDownList extends ConstraintLayout implements IDropDownList, Observer {
    public boolean isHiden = true, isExpand = false, isSetAdapter = false;

    private TextView title;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView background, iconExpand, iconBtnBack;

    private String defaultTitle = "Title";
    private Collection collection;
    private Stack<Collection> stackCollections;

    public DropDownList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
        initializationListeners();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.drop_down_list, this);
        collection = new ArrayList();
        stackCollections = new Stack();

        title = findViewById(R.id.titleDDL);
        background = findViewById(R.id.backgroundDDL);
        iconExpand = findViewById(R.id.iconExpandDDL);
        progressBar = findViewById(R.id.progressBarDDL);
        iconBtnBack = findViewById(R.id.iconBtnBackDDL);
        recyclerView = findViewById(R.id.recyclerViewDDL);
    }

    private void initializationStyles() {
        background.setImageDrawable(new FieldBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f}));
    }

    private void initializationListeners() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmptyToBackStack() && CURRENT_SIZE_STACK() > 1) {
                    if (isHiden) {
                        expandList();
                    } else {
                        popToBackStack();
                        title.setText(defaultTitle);
                        refreshCollection(stackCollections.lastElement());
                    }
                } else {
                    if (isHiden)
                        expandList();
                    else
                        hideList();
                }
            }
        };

        title.setOnClickListener(onClickListener);
        iconBtnBack.setOnClickListener(onClickListener);
    }

    public void setIconExpand(Drawable iconExpand) {
        this.iconExpand.setImageDrawable(iconExpand);
    }

    public void setIconBtnBack(Drawable iconBtnBack) {
        this.iconBtnBack.setImageDrawable(iconBtnBack);
    }

    public void setBackground(Drawable background) {
        this.background.setImageDrawable(background);
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
        this.title.setText(defaultTitle);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if (adapter instanceof AdapterDropDownList)
            ((AdapterDropDownList) adapter).setDropDownList(this);
    }

    @Override
    public void hideList() {
        if (adapter != null) {
            isHiden = true;
            isExpand = false;
            rotateIcon(0f);
            visibilityBtnBack(false);
            clearRecyclerView((AdapterDropDownList) adapter);
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
                refreshCollection(stackCollections.lastElement());
            }
        }
    }

    @Override
    public View getProgressBar() {
        return progressBar;
    }

    @Override
    public void clearRecyclerView(AdapterDropDownList adapter) {
        adapter.clearRecyclerView();
    }

    @Override
    public void refreshCollection(Collection collection) {
        if (adapter instanceof AdapterDropDownList) {
            visibilityBtnBack(CURRENT_SIZE_STACK() > 1);
            ((AdapterDropDownList) adapter).refreshCollection(collection);
        }
    }

    @Override
    public void pushToBackStack(Collection collection) {
        if (CURRENT_SIZE_STACK() < MAX_SIZE_STACK() && !isContainsToBackStack(collection)) {
            this.stackCollections.push(new ArrayList<>(collection));
        }
    }

    @Override
    public Collection popToBackStack() {
        return this.stackCollections.pop();
    }

    @Override
    public boolean isEmptyToBackStack() {
        return this.stackCollections.isEmpty();
    }

    @Override
    public boolean isContainsToBackStack(Collection collection) {
        return this.stackCollections.contains(collection);
    }

    @Override
    public int CURRENT_SIZE_STACK() {
        return this.stackCollections.size();
    }

    @Override
    public int MAX_SIZE_STACK() {
        return 2;
    }

    private void rotateIcon(float angle) {
        iconExpand.setRotation(angle);
    }

    private void visibilityBtnBack(boolean b) {
        if (b) {
            iconBtnBack.setVisibility(VISIBLE);
        } else {
            iconBtnBack.setVisibility(GONE);
        }
    }

    @Override
    public void update(Object object) {
        if (object instanceof Collection) {
            if (((Collection) object).size() > 0) {
                pushToBackStack((Collection) object);
                refreshCollection((Collection) object);
            } else
                hideList();
        }
    }
}