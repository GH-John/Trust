package com.application.arenda.Entities.Announcements.InsertAnnouncement.Categories;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Models.IModel;
import com.application.arenda.Entities.Models.ModelCategory;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.Entities.Utils.Glide.GlideUtils;
import com.application.arenda.R;
import com.github.florent37.expansionpanel.ExpansionLayout;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesVH extends BaseViewHolder {
    @Nullable
    @BindView(R.id.categoryTitle)
    TextView categoryTitle;

    @Nullable
    @BindView(R.id.categoryIcon)
    ImageView categoryIcon;

    @Nullable
    @BindView(R.id.rvItemCategory)
    RecyclerView rvItemCategory;

    @Nullable
    @BindView(R.id.categoryExpansionLayout)
    ExpansionLayout categoryExpansionLayout;


    private ModelCategory modelCategory;

    public CategoriesVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static CategoriesVH create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_category_item, parent, false);
        return new CategoriesVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_category_item;
    }

    @Override
    public void onBind(IModel model, int position) {
        categoryExpansionLayout.collapse(false);

        modelCategory = (ModelCategory) model;

        categoryTitle.setText(modelCategory.getName());

        GlideUtils.loadVector(itemView.getContext(), Uri.parse(modelCategory.getIconUri()), categoryIcon);
    }

    public ExpansionLayout getCategoryExpansionLayout() {
        return categoryExpansionLayout;
    }

    public void setOnClickListener(OnClickItemCategory listener) {
        categoryExpansionLayout.addListener((expansionLayout, expanded) -> {
            if (expanded)
                listener.inflateRecyclerViewOnClick(modelCategory.getID(), rvItemCategory);
        });
    }

    public interface OnClickItemCategory {
        void inflateRecyclerViewOnClick(long idCategory, RecyclerView rvItemCategory);
    }
}