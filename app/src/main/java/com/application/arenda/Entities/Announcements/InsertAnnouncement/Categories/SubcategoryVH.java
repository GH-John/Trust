package com.application.arenda.Entities.Announcements.InsertAnnouncement.Categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.IModel;
import com.application.arenda.Entities.Models.ModelSubcategory;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.Entities.RecyclerView.OnItemClick;
import com.application.arenda.R;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubcategoryVH extends BaseViewHolder {
    @Nullable
    @BindView(R.id.subcategoryTitle)
    TextView subcategoryTitle;

    private ModelSubcategory modelSubcategory;

    public SubcategoryVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static SubcategoryVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_subcategory_item, parent, false);
        return new SubcategoryVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_subcategory_item;
    }

    @Override
    public void onBind(IModel model, int position) {
        modelSubcategory = (ModelSubcategory) model;

        subcategoryTitle.setText(modelSubcategory.getName());
    }

    public long getIdSubcategory() {
        return modelSubcategory.getID();
    }

    public long getIdCategory() {
        return modelSubcategory.getIdCategory();
    }

    public void setOnClickListener(OnItemClick itemClick){
        itemView.setOnClickListener(v -> itemClick.onClick(SubcategoryVH.this, modelSubcategory));
    }
}