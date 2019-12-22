package com.application.arenda.UI.DropDownList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList.ModelItemContent;
import com.application.arenda.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropDownList extends RelativeLayout implements IDropDownList {
    public boolean isHiden = true, isExpand = false;

    @Nullable
    @BindView(R.id.progressBarDDL)
    ProgressBar progressBar;

    @Nullable
    @BindView(R.id.recyclerViewDDL)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.titleDDL)
    TextView title;

    @Nullable
    @BindView(R.id.textErrorDDL)
    TextView textError;

    @Nullable
    @BindView(R.id.backgroundDDL)
    ImageView background;

    @Nullable
    @BindView(R.id.iconExpandDDL)
    ImageView iconExpand;

    @Nullable
    @BindView(R.id.iconBtnBackDDL)
    ImageView iconBtnBack;

    private RecyclerView.Adapter adapter;
    private boolean isSetAdapter = false;
    private OnClickListener onClickListener;
    private Stack<Collection<ModelItemContent>> stackCollections = new Stack();
    private String defaultTitle = "";
    private String defaultError = "";

    public DropDownList(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.drop_down_list, this);
        ButterKnife.bind(this);

        initializationStyles();
    }

    private void initializationStyles() {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(30);
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
    public String getDefaultTitle() {
        return this.defaultTitle;
    }

    @Override
    public void setDefaultTitle(@NonNull String defaultTitle) {
        this.defaultTitle = defaultTitle;
        this.title.setText(this.defaultTitle);
    }

    @Override
    public String getDefaultError() {
        return this.defaultError;
    }

    @Override
    public void setDefaultError(@NonNull String defaultError) {
        this.defaultError = defaultError;
        this.textError.setText(this.defaultError);
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
            } else if (!isEmptyBackStack()) {
                rewriteCollection(stackCollections.lastElement());
            }
        }
    }

    @Override
    public void progressBarActive(boolean b) {
        if (b) {
            progressBar.setVisibility(VISIBLE);
        } else
            progressBar.setVisibility(GONE);
    }

    @Override
    public void clearRecyclerView(@NonNull AdapterDropDownList adapter) {
        adapter.clearRecyclerView();
    }

    @Override
    public void rewriteCollection(@NonNull Collection<ModelItemContent> collection) {
        if (adapter instanceof AdapterDropDownList) {
            visibilityBtnBack(CURRENT_SIZE_STACK() > 1);
            ((AdapterDropDownList) adapter).rewriteCollection(collection);
        }
    }

    @Override
    public void pushToStack(@NonNull Collection<ModelItemContent> collection) {
        if (CURRENT_SIZE_STACK() < MAX_SIZE_STACK() && !isContainsToStack(collection)) {
            this.stackCollections.push(new ArrayList<>(collection));
        }
    }

    @Override
    public Collection<ModelItemContent> popToStack() {
        return this.stackCollections.pop();
    }

    @Override
    public boolean isEmptyBackStack() {
        return this.stackCollections.isEmpty();
    }

    @Override
    public boolean isContainsToStack(@NonNull Collection<ModelItemContent> collection) {
        return this.stackCollections.contains(collection);
    }

    @Override
    public Collection<ModelItemContent> getFirstElementToStack() {
        return this.stackCollections.firstElement();
    }

    @Override
    public Collection<ModelItemContent> getLastElementToStack() {
        return this.stackCollections.lastElement();
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
    public void setOnClickLastElement(@NonNull OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void setOnClickBack(@NonNull OnClickListener onClickBack) {
        title.setOnClickListener(onClickBack);
        iconBtnBack.setOnClickListener(onClickBack);
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
}