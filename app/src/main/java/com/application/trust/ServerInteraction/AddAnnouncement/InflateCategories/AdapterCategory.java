package com.application.trust.ServerInteraction.AddAnnouncement.InflateCategories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trust.R;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {
    private List<ItemContent> itemContents;
    private int idPatternLayout;
    private Context context;

    public AdapterCategory(int idPatternLayout, List<ItemContent> itemContents) {
        this.idPatternLayout = idPatternLayout;
        this.itemContents = itemContents;
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

    @NonNull
    @Override
    public AdapterCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.idPatternLayout, parent, false);
        this.context = parent.getContext();
        return new AdapterCategory.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.ViewHolder holder, int position) {
        final ItemContent itemContent = this.itemContents.get(position);
        holder.nameCategory.setText(itemContent.getName());
        holder.iconCategory.setImageResource(R.drawable.ic_category_plus);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "item", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemContents.size();
    }
}