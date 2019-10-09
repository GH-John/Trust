package com.application.trust.CustomComponents.Panels.SideBar.ItemList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trust.R;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>
{
    private List<ContentItemList> contentItemLists;
    private int idPatternLayout;
    private Context context;

    public ItemListAdapter(int idPatternLayout, List<ContentItemList> contentItemList){
        this.idPatternLayout = idPatternLayout;
        this.contentItemLists = contentItemList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconItemList,
                  backgroundItemList;
        TextView nameItemList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconItemList = itemView.findViewById(R.id.iconItemList);
            backgroundItemList = itemView.findViewById(R.id.backgroundItemList);
            nameItemList = itemView.findViewById(R.id.nameItemList);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.idPatternLayout, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(v);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ContentItemList contentItemList = this.contentItemLists.get(position);
        holder.iconItemList.setImageResource(contentItemList.getIdIconPanel());
        holder.nameItemList.setText(context.getString(contentItemList.getIdNamePanel()));
        holder.backgroundItemList.setImageDrawable(contentItemList.getStylePanel());

        holder.backgroundItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentItemLists.size();
    }
}