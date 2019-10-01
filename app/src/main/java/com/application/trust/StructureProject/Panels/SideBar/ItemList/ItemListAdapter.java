package com.application.trust.StructureProject.Panels.SideBar.ItemList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trust.R;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>
{
    private List<ItemList> itemLists;
    private int idPatternLayout;
    private Context context;

    public ItemListAdapter(int idPatternLayout, List<ItemList> itemList){
        this.idPatternLayout = idPatternLayout;
        this.itemLists = itemList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemList itemList = this.itemLists.get(position);
        holder.iconItemList.setImageResource(itemList.getIdIconItemList());
        holder.nameItemList.setText(context.getString(itemList.getIdNameItemList()));
        holder.backgroundItemList.setImageResource(itemList.getIdStyleItemList());

        holder.backgroundItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }
}