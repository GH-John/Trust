package com.application.arenda.UI.DropDownList;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.ServerInteraction.InsertAnnouncement.InflateDropDownList.ModelItemContent;

import java.util.Collection;

public interface IDropDownList {

    void setTitle(@NonNull String title);

    void setDefaultTitle(@NonNull String defaultTitle);

    void setAdapter(@NonNull RecyclerView.Adapter adapter);

    void hideList();

    void expandList();

    View getProgressBar();

    void clearRecyclerView(@NonNull AdapterDropDownList adapter);

    void refreshCollection(@NonNull Collection<ModelItemContent> collection);

    void pushToBackStack(@NonNull Collection<ModelItemContent> collection);

    Collection<ModelItemContent> popToBackStack();

    boolean isEmptyBackStack();

    boolean isContainsToBackStack(@NonNull Collection<ModelItemContent> collection);

    void setOnClickLastElement(View.OnClickListener onClickListener);

    void setError(@NonNull String error);

    void clearError();

    void visibleError(boolean visible);

    int CURRENT_SIZE_STACK();

    int MAX_SIZE_STACK();
}