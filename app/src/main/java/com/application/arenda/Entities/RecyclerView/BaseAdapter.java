package com.application.arenda.Entities.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Models.IModel;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<M extends IModel, V extends BaseViewHolder> extends RecyclerView.Adapter<V> implements RVAdapter<M> {
    private boolean isLoading = false;

    private List<M> collection = new ArrayList<>();

    public BaseAdapter() {
        setHasStableIds(true);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setLoading(boolean b) {
        isLoading = b;
    }

    @Override
    public void addToCollection(List<M> collection) {
        if (collection != null && collection.size() > 0) {
            final int start = getItemCount();

            this.collection.addAll(collection);

            notifyItemRangeInserted(start, getItemCount());
        }

        setLoading(false);
    }

    @Override
    public void addToCollection(M model) {
        if (model != null) {
            this.collection.add(model);
            notifyItemInserted(getItemCount() - 1);
        }

        setLoading(false);
    }

    @Override
    public void removeFromCollection(int position) {
        if (position > -1) {
            this.collection.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void rewriteCollection(List<M> collection) {
        if (collection != null && collection.size() > 0) {
            this.collection.clear();
            this.collection.addAll(collection);

            notifyDataSetChanged();
        }

        setLoading(false);
    }

    @Override
    public M getItem(int position) {
        return this.collection.get(position);
    }

    @Override
    public M getLastItem() {
        return this.collection.get(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return collection.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}