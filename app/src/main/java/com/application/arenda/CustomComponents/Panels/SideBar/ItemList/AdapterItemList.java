package com.application.arenda.CustomComponents.Panels.SideBar.ItemList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;

import java.util.List;

public class AdapterItemList extends RecyclerView.Adapter<AdapterItemList.ViewHolder>
{
    private List<ContentItemList> contentItemLists;
    private int idPatternLayout;
    private Context context;

    public AdapterItemList(int idPatternLayout, List<ContentItemList> contentItemList){
        this.idPatternLayout = idPatternLayout;
        this.contentItemLists = contentItemList;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconItemList,
                  backgroundItemList;
        TextView nameItemList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundItemList = itemView.findViewById(R.id.panelItemList);
            iconItemList = itemView.findViewById(R.id.iconItemList);
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