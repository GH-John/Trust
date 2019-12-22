package com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;
import com.application.arenda.UI.DropDownList.AdapterDropDownList;
import com.application.arenda.UI.DropDownList.IDropDownList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> implements AdapterDropDownList {
    private Context context;
    private int idPatternLayout;
    private IDropDownList dropDownList;
    private LoadingCategories categories = new LoadingCategories();
    private List<ModelItemContent> models;

    public AdapterRecyclerView(int idPatternLayout, List<ModelItemContent> models) {
        this.idPatternLayout = idPatternLayout;
        this.models = new ArrayList<>(models);

        setHasStableIds(true);
    }

    @Override
    public void setDropDownList(IDropDownList dropDownList) {
        this.dropDownList = dropDownList;
    }

    @Override
    public void clearRecyclerView() {
        this.notifyItemRangeRemoved(0, this.models.size());
        this.models.clear();
    }

    @Override
    public Collection getModels() {
        return this.models;
    }

    @Override
    public void rewriteCollection(Collection collection) {
        this.models.clear();
        this.models.addAll(collection);
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
        final ModelItemContent model = this.models.get(position);
        holder.nameCategory.setText(model.getName());

        holder.layout.setOnClickListener(v -> {
            dropDownList.setTitle(model.getName());
            if (!(dropDownList.CURRENT_SIZE_STACK() == dropDownList.MAX_SIZE_STACK())) {

                dropDownList.progressBarActive(true);
                categories.loadingSubcategories(context, model.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<ModelItemContent>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(List<ModelItemContent> modelItemContents) {
                                dropDownList.rewriteCollection(modelItemContents);

                                dropDownList.visibleError(true);
                                if (modelItemContents.size() > 0) {
                                    dropDownList.pushToStack(modelItemContents);
                                    dropDownList.rewriteCollection(modelItemContents);
                                } else {
                                    dropDownList.visibleError(true);
                                    dropDownList.hideList();
                                }

                                dropDownList.progressBarActive(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                dropDownList.progressBarActive(false);
                            }

                            @Override
                            public void onComplete() {
                                dropDownList.progressBarActive(false);
                            }
                        });
            } else {
                dropDownList.hideList();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return models.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.nameCategory)
        TextView nameCategory;

        @Nullable
        @BindView(R.id.layoutPatternCategory)
        FrameLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}