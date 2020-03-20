package com.application.arenda.UI.Components.SideBar.ItemList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.Entities.Announcements.Models.IModel;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.Entities.RecyclerView.OnItemClick;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListVH extends BaseViewHolder {
    @Nullable
    @BindView(R.id.iconItemList)
    ImageView iconItemList;

    @Nullable
    @BindView(R.id.panelItemList)
    ImageView panelItemList;

    @Nullable
    @BindView(R.id.nameItemList)
    TextView nameItemList;

    private int position;
    private ModelItemList model;

    public ItemListVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static ItemListVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sb_pattern_item_list, parent, false);
        return new ItemListVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.sb_pattern_item_list;
    }

    @Override
    public void onBind(IModel model, int position) {
        this.model = (ModelItemList) model;
        this.position = position;

        iconItemList.setImageResource(((ModelItemList) model).getIdIconPanel());
        nameItemList.setText(itemView.getContext().getString(((ModelItemList) model).getIdNamePanel()));
        panelItemList.setImageDrawable(((ModelItemList) model).getStylePanel());
    }

    public void setItemViewClick(OnItemClick itemViewClick) {
        itemView.setOnClickListener(v -> itemViewClick.onClick(this, model));
    }
}
