package com.application.arenda.UI.DropDownList;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList.ModelItemContent;

import java.util.Collection;

public interface IDropDownList {

    void setTitle(@NonNull String title);

    String getDefaultTitle();

    void setDefaultTitle(@NonNull String defaultTitle);

    String getDefaultError();

    void setDefaultError(@NonNull String defaultError);

    void setError(@NonNull String error);

    void setAdapter(@NonNull RecyclerView.Adapter adapter);

    void hideList();

    void expandList();

    void progressBarActive(boolean b);

    void clearRecyclerView(@NonNull AdapterDropDownList adapter);

    void rewriteCollection(@NonNull Collection<ModelItemContent> collection);

    void pushToStack(@NonNull Collection<ModelItemContent> collection);

    Collection<ModelItemContent> popToStack();

    Collection<ModelItemContent> getFirstElementToStack();

    Collection<ModelItemContent> getLastElementToStack();

    boolean isEmptyBackStack();

    boolean isContainsToStack(@NonNull Collection<ModelItemContent> collection);

    void setOnClickLastElement(@NonNull View.OnClickListener onClickListener);

    void setOnClickBack(@NonNull View.OnClickListener onClickBack);

    void clearError();

    void visibleError(boolean visible);

    int CURRENT_SIZE_STACK();

    int MAX_SIZE_STACK();
}