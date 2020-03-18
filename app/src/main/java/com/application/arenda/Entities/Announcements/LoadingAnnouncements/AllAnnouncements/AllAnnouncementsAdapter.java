package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Announcements.TypesAnnouncements;
import com.application.arenda.Entities.RecyclerView.ProgressViewHolder;
import com.application.arenda.Entities.RecyclerView.RVAdapter;
import com.application.arenda.R;

import java.util.ArrayList;
import java.util.List;

public class AllAnnouncementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RVAdapter {
    private final Context context;
    private boolean isLoading = false;

    private AllAnnouncementsViewHolder.OnItemClick onItemClick;
    private LayoutInflater layoutInflater;
    private List<ModelAllAnnouncement> collection = new ArrayList<>();

    private int ITEM_LOADING = -1;

    public AllAnnouncementsAdapter(final Context context) {
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
    public void addToCollection(List<ModelAllAnnouncement> collection) {
        setLoading(false);

        if (collection != null && collection.size() > 0) {
            final int start = getItemCount() + 1;

            this.collection.addAll(collection);

            notifyItemRangeInserted(start, getItemCount());
        }
    }

    @Override
    public void addToCollection(ModelAllAnnouncement model) {
        setLoading(false);

        if (model != null) {
            this.collection.add(model);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    @Override
    public void removeFromCollection(int position) {
        if (position > -1) {
            this.collection.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void rewriteCollection(List<ModelAllAnnouncement> collection) {
        if (collection != null && collection.size() > 0) {
            this.collection.clear();
            this.collection.addAll(collection);

            notifyDataSetChanged();
        }

        setLoading(false);
    }

    @Override
    public ModelAllAnnouncement getItem(int position) {
        return this.collection.get(position);
    }

    @Override
    public ModelAllAnnouncement getLastItem() {
        return this.collection.get(getItemCount() - 1);
    }

    public void onItemClick(AllAnnouncementsViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_LOADING) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_progress_bar, parent, false);
            return new ProgressViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_announcement, parent, false);
            return new AllAnnouncementsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final int getViewType = holder.getItemViewType();

        if (getViewType == ITEM_LOADING) {
            ((ProgressViewHolder) holder).onBind();
        } else {
            ((AllAnnouncementsViewHolder) holder).onBind(context, this.collection.get(position), position);
            ((AllAnnouncementsViewHolder) holder).setOnItemViewClick(onItemClick);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1 && isLoading()) ? ITEM_LOADING : TypesAnnouncements.ALL_USERS.ordinal();
//        return TypesAnnouncements.ALL_USERS.ordinal();
    }

    @Override
    public long getItemId(int position) {
        return collection.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return collection != null ? collection.size() : 0;
    }
}