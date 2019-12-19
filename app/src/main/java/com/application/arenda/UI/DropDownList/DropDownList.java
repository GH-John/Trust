package com.application.arenda.UI.DropDownList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.InsertAnnouncement.InflateDropDownList.ModelItemContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class DropDownList extends ConstraintLayout implements IDropDownList, Observer {
    public boolean isHiden = true, isExpand = false;
    private boolean isSetAdapter = false;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView title, textError;
    private RecyclerView.Adapter adapter;
    private ImageView background, iconExpand, iconBtnBack;

    private String defaultTitle = "Title";
    private OnClickListener onClickListener;
    private Stack<Collection<ModelItemContent>> stackCollections;

    public DropDownList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
        initializationListeners();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.drop_down_list, this);
        stackCollections = new Stack();

        defaultTitle = getResources().getString(R.string.text_some_text);

        title = findViewById(R.id.titleDDL);
        textError = findViewById(R.id.textErrorDDL);
        background = findViewById(R.id.backgroundDDL);
        iconExpand = findViewById(R.id.iconExpandDDL);
        progressBar = findViewById(R.id.progressBarDDL);
        iconBtnBack = findViewById(R.id.iconBtnBackDDL);
        recyclerView = findViewById(R.id.recyclerViewDDL);
    }

    private void initializationStyles() {
        background.setImageDrawable(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(30);
    }

    private void initializationListeners() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyBackStack() && CURRENT_SIZE_STACK() > 1) {
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

    public void setBackground(@NonNull Drawable background) {
        this.background.setImageDrawable(background);
    }

    @Override
    public void setTitle(@NonNull String title) {
        this.title.setText(title);
    }

    @Override
    public void setDefaultTitle(@NonNull String defaultTitle) {
        this.defaultTitle = defaultTitle;
        this.title.setText(defaultTitle);
    }

    @Override
    public void setAdapter(@NonNull RecyclerView.Adapter adapter) {
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

            if (!(CURRENT_SIZE_STACK() == MAX_SIZE_STACK())) {
                visibleError(true);
            } else {
                visibleError(false);
                if (onClickListener != null)
                    onClickListener.onClick(this);
            }
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
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
            } else if (isEmptyBackStack()) {
                refreshCollection(stackCollections.lastElement());
            }
        }
    }

    @Override
    public View getProgressBar() {
        return progressBar;
    }

    @Override
    public void clearRecyclerView(@NonNull AdapterDropDownList adapter) {
        adapter.clearRecyclerView();
    }

    @Override
    public void refreshCollection(@NonNull Collection<ModelItemContent> collection) {
        if (adapter instanceof AdapterDropDownList) {
            visibilityBtnBack(CURRENT_SIZE_STACK() > 1);
            ((AdapterDropDownList) adapter).refreshCollection(collection);
        }
    }

    @Override
    public void pushToBackStack(@NonNull Collection<ModelItemContent> collection) {
        if (CURRENT_SIZE_STACK() < MAX_SIZE_STACK() && !isContainsToBackStack(collection)) {
            this.stackCollections.push(new ArrayList<>(collection));
        }
    }

    @Override
    public Collection<ModelItemContent> popToBackStack() {
        return this.stackCollections.pop();
    }

    @Override
    public boolean isEmptyBackStack() {
        return !this.stackCollections.isEmpty();
    }

    @Override
    public boolean isContainsToBackStack(@NonNull Collection<ModelItemContent> collection) {
        return this.stackCollections.contains(collection);
    }

    public int getIdSelectedElement() {
        int id = 0;
        for (ModelItemContent model : stackCollections.lastElement()) {
            if (model.getName().contentEquals(title.getText()))
                id = model.getId();
        }
        return id;
    }

    @Override
    public void setOnClickLastElement(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void setError(@NonNull String error) {
        textError.setText(error);
    }

    @Override
    public void clearError() {
        textError.setText("");
    }

    @Override
    public void visibleError(boolean visible) {
        if (visible) textError.setVisibility(View.VISIBLE);
        else textError.setVisibility(View.GONE);
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
    public void update(@NonNull Object object) {
        if (object instanceof Collection) {
            visibleError(true);
            if (((Collection<ModelItemContent>) object).size() > 0) {
                pushToBackStack((Collection) object);
                refreshCollection((Collection) object);
            } else {
                setError(String.valueOf(R.string.error_check_internet_connect));
                visibleError(true);
                hideList();
            }
        }
    }
}