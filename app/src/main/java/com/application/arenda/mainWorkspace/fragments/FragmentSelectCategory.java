package com.application.arenda.mainWorkspace.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;
import com.application.arenda.entities.announcements.categories.CategoriesAdapter;
import com.application.arenda.entities.announcements.categories.EventSendID;
import com.application.arenda.entities.announcements.categories.SubcategoriesAdapter;
import com.application.arenda.entities.models.ModelSubcategory;
import com.application.arenda.entities.serverApi.announcement.ApiAnnouncement;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FragmentSelectCategory extends Fragment implements AdapterActionBar {

    @SuppressLint("StaticFieldLeak")
    private static FragmentSelectCategory instanse;

    @Nullable
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;

    private ImageButton itemCategoryClose;
    private TextView itemTitleSelectCategory;

    private Unbinder unbinder;

    private ApiAnnouncement api;
    private CategoriesAdapter categoriesAdapter;
    private CompositeDisposable disposable;


    private FragmentSelectCategory() {
        disposable = new CompositeDisposable();
    }

    public static FragmentSelectCategory getInstance() {
        if (instanse == null)
            instanse = new FragmentSelectCategory();

        return instanse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_category, container, false);

        unbinder = ButterKnife.bind(this, view);

        initComponents();
        initListeners();
        initAdapters();

        return view;
    }

    private void initComponents() {
        categoriesAdapter = new CategoriesAdapter();
        api = ApiAnnouncement.getInstance(getContext());

        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void initListeners() {
        categoriesAdapter.setOnClickCategory((idCategory, rvItemCategory) -> {

            if (rvItemCategory.getAdapter() == null || rvItemCategory.getAdapter().getItemCount() == 0) {

                SubcategoriesAdapter subcategoriesAdapter = new SubcategoriesAdapter();

                subcategoriesAdapter.setItemClick((viewHolder, model, pos) -> {
                    EventBus.getDefault().post(new EventSendID((int) model.getID(), ((ModelSubcategory) model).getName()));

                    ContainerFragments.getInstance(getContext()).popBackStack();
                });

                disposable.add(api.loadSubcategories(idCategory)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                subcategories -> {
                                    subcategoriesAdapter.addToCollection(subcategories);
                                    rvItemCategory.setLayoutManager(new LinearLayoutManager(rvItemCategory.getContext(), RecyclerView.VERTICAL, false));
                                    rvItemCategory.setAdapter(subcategoriesAdapter);
                                }, Timber::e));
            }
        });
    }

    private void initAdapters() {
        disposable.add(api.loadCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> {
                            categoriesAdapter.addToCollection(categories);
                            rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                            rvCategories.setAdapter(categoriesAdapter);
                        }, Timber::e));
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_select_category;
    }

    @Override
    public void initComponentsActionBar(ViewGroup viewGroup) {
        itemCategoryClose = viewGroup.findViewById(R.id.itemCategoryClose);
        itemTitleSelectCategory = viewGroup.findViewById(R.id.itemTitleSelectCategory);
    }

    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemCategoryClose.setOnClickListener(v -> ContainerFragments.getInstance(getContext()).popBackStack());
    }
}