package com.application.arenda.UI.Components.SideBar.ItemList;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.Entities.RecyclerView.OnItemClick;

public class ItemListAdapter extends BaseAdapter<ModelItemList, ItemListVH> {

    private OnItemClick onItemClick;

    @NonNull
    @Override
    public ItemListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ItemListVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListVH holder, final int position) {
        holder.onBind(getItem(position), position);

        if (onItemClick != null)
            holder.setItemViewClick(onItemClick);
    }

    public void setItemViewClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}