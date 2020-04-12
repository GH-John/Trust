package com.application.arenda.Entities.Announcements.Categories;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.Category;
import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

public class CategoriesAdapter extends BaseAdapter<Category, CategoriesVH> {
    private final ExpansionLayoutCollection expansionLayoutCollection;

    private CategoriesVH.OnClickItemCategory listener;

    public CategoriesAdapter() {
        expansionLayoutCollection = new ExpansionLayoutCollection();
        expansionLayoutCollection.openOnlyOne(true);
    }

    @NonNull
    @Override
    public CategoriesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CategoriesVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesVH holder, int position) {
        holder.onBind(getItem(position), position);

        if (listener != null)
            holder.setOnClickListener(listener);

        expansionLayoutCollection.add(holder.getCategoryExpansionLayout());
    }

    public void setOnClickCategory(CategoriesVH.OnClickItemCategory listener) {
        this.listener = listener;
    }
}