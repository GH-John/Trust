package com.application.arenda.ServerInteraction.InsertAnnouncement.InflateDropDownList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.UI.DropDownList.AdapterDropDownList;
import com.application.arenda.UI.DropDownList.IDropDownList;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> implements AdapterDropDownList {
    private Context context;
    private int idPatternLayout;
    private IDropDownList dropDownList;
    private List<ModelItemContent> collection;

    public AdapterRecyclerView(int idPatternLayout, Collection<ModelItemContent> collection) {
        this.idPatternLayout = idPatternLayout;
        this.collection = new ArrayList<>(collection);

        setHasStableIds(true);
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
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.idPatternLayout, parent, false);
        this.context = parent.getContext();

        return new AdapterRecyclerView.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerView.ViewHolder holder, int position) {
        final ModelItemContent modelItemContent = this.collection.get(position);
        holder.nameCategory.setText(modelItemContent.getName());

        holder.relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownList.setTitle(modelItemContent.getName());
                if (!(dropDownList.CURRENT_SIZE_STACK() == dropDownList.MAX_SIZE_STACK()))
                    RecipientCategories.getCollectionSubcategories(context, modelItemContent.getId(),
                            dropDownList.getProgressBar(), (Observer) dropDownList);
                else {
                    dropDownList.hideList();
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return collection.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return collection != null ? collection.size() : 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameCategory;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = itemView.findViewById(R.id.nameCategory);
            relativeLayout = itemView.findViewById(R.id.layoutPatternCategory);
        }
    }
}