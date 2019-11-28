package com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.CustomComponents.DropDownList.IDropDownList;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> implements AdapterDropDownList {
    private Context context;
    private int idPatternLayout;
    private Collection collection;
    private IDropDownList dropDownList;

    public AdapterCategory(int idPatternLayout, Collection collection) {
        this.idPatternLayout = idPatternLayout;
        this.collection = new ArrayList<>();
        this.collection.addAll(collection);
    }

    @Override
    public void setDropDownList(IDropDownList dropDownList) {
        this.dropDownList = dropDownList;
    }

    @Override
    public void clearRecyclerView() {
        this.notifyItemRangeRemoved(0, this.collection.size());
        this.collection.clear();
    }

    @Override
    public Collection getCollection() {
        return this.collection;
    }

    @Override
    public void refreshCollection(Collection collection) {
        this.collection.clear();
        this.collection.addAll(collection);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.idPatternLayout, parent, false);
        this.context = parent.getContext();

        return new AdapterCategory.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.ViewHolder holder, int position) {
        final ItemContent itemContent = ((List<ItemContent>) this.collection).get(position);
        holder.nameCategory.setText(itemContent.getName());
        holder.iconCategory.setImageResource(R.drawable.ic_category_plus);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InflateDropDownList.getCollectionSubcategories(context, itemContent.getId(),
                        dropDownList.getProgressBar(), (Observer) dropDownList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameCategory;
        ImageView iconCategory;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = itemView.findViewById(R.id.nameCategory);
            iconCategory = itemView.findViewById(R.id.iconCategory);
            relativeLayout = itemView.findViewById(R.id.layoutPatternCategory);
        }
    }
}