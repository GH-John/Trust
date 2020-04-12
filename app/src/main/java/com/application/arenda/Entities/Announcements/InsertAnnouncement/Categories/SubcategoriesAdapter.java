package com.application.arenda.Entities.Announcements.InsertAnnouncement.Categories;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.ModelSubcategory;
import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.Entities.RecyclerView.OnItemClick;

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

        if(itemClick != null)
            holder.setOnClickListener(itemClick);
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }
}