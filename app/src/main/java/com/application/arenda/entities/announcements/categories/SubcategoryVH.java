package com.application.arenda.entities.announcements.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelSubcategory;
import com.application.arenda.entities.recyclerView.BaseViewHolder;
import com.application.arenda.entities.recyclerView.OnItemClick;
import com.application.arenda.R;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubcategoryVH extends BaseViewHolder {
    @Nullable
    @BindView(R.id.subcategoryTitle)
    TextView subcategoryTitle;

    private ModelSubcategory modelSubcategory;
    private int position;

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
        if(model == null)
            return;
        this.position= position;
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
        itemView.setOnClickListener(v -> itemClick.onClick(SubcategoryVH.this, modelSubcategory, position));
    }
}