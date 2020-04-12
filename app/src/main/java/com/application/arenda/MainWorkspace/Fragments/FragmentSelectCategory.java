package com.application.arenda.MainWorkspace.Fragments;

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

import com.application.arenda.Entities.Announcements.ApiAnnouncement;
import com.application.arenda.Entities.Announcements.Categories.CategoriesAdapter;
import com.application.arenda.Entities.Announcements.Categories.EventSendID;
import com.application.arenda.Entities.Announcements.Categories.SubcategoriesAdapter;
import com.application.arenda.Entities.Models.Category;
import com.application.arenda.Entities.Models.Subcategory;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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

    private ApiAnnouncement apiAnnouncement;
    private CategoriesAdapter categoriesAdapter;
    private CompositeDisposable compositeDisposable;


    private FragmentSelectCategory() {
        compositeDisposable = new CompositeDisposable();
    }

    public static FragmentSelectCategory getInnstance() {
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
        apiAnnouncement = ApiAnnouncement.getInstance();

        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void initListeners() {
        categoriesAdapter.setOnClickCategory((idCategory, rvItemCategory) -> {

            if (rvItemCategory.getAdapter() == null || rvItemCategory.getAdapter().getItemCount() == 0) {

                SubcategoriesAdapter adapter = new SubcategoriesAdapter();

                adapter.setItemClick((viewHolder, model) -> {
                    EventBus.getDefault().post(new EventSendID(model.getObjectId(), ((Subcategory) model).getName()));

                    ContainerFragments.getInstance(getContext()).popBackStack();
                });

                apiAnnouncement.getSubcategories(getContext(), idCategory)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Subcategory>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(List<Subcategory> modelSubcategories) {
                                rvItemCategory.setLayoutManager(new LinearLayoutManager(rvItemCategory.getContext(), RecyclerView.VERTICAL, false));
                                adapter.addToCollection(modelSubcategories);
                                rvItemCategory.setAdapter(adapter);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }

    @SuppressLint("CheckResult")
    private void initAdapters() {
        apiAnnouncement.getCategories(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Category>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        categoriesAdapter.addToCollection(categories);
                        rvCategories.setAdapter(categoriesAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
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