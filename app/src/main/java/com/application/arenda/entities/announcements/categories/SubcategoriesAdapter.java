package com.application.arenda.entities.announcements.categories;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelSubcategory;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class SubcategoriesAdapter extends BaseAdapter<ModelSubcategory, SubcategoryVH> {
    private OnItemClick itemClick;

    @NonNull
    @Override
    public SubcategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SubcategoryVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryVH holder, int position) {
        holder.onBind(getItem(position), position);

        if (itemClick != null)
            holder.setOnClickListener(itemClick);
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }
}