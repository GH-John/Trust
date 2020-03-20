package com.application.arenda.MainWorkspace.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList.AdapterRecyclerView;
import com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList.LoadingCategories;
import com.application.arenda.Entities.Announcements.InsertAnnouncement.InsertAnnouncement;
import com.application.arenda.Entities.Announcements.Models.ModelInsertAnnouncement;
import com.application.arenda.Entities.Utils.DecimalDigitsInputFilter;
import com.application.arenda.Entities.Utils.Network.ServerUtils;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;
import com.application.arenda.UI.Components.SideBar.ItemSideBar;
import com.application.arenda.UI.Components.SideBar.SideBar;
import com.application.arenda.UI.ContainerImg.ContainerFiller;
import com.application.arenda.UI.ContainerImg.ContainerSelectedImages;
import com.application.arenda.UI.ContainerImg.Galery.AdapterGalery;
import com.application.arenda.UI.DropDownList.DropDownList;
import com.application.arenda.UI.Style.SetBtnStyle;
import com.application.arenda.UI.Style.SetFieldStyle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class FragmentInsertAnnouncement extends Fragment implements ItemSideBar, AdapterActionBar, AdapterGalery {
    @SuppressLint("StaticFieldLeak")
    private static FragmentInsertAnnouncement fragmentInsertAnnouncement;
    private final int SELECTED_IMG_CODE = 1001;

    @Nullable
    @BindView(R.id.groupPhones)
    Group groupPhones;
    @Nullable
    @BindView(R.id.itemBurgerMenu)
    ImageView itemBurgerMenu;
    @Nullable
    @BindView(R.id.dropDownList)
    DropDownList dropDownList;
    @Nullable
    @BindView(R.id.groupAdditionalFields)
    Group groupAdditionalFields;
    @Nullable
    @BindView(R.id.textLimitAddPhotos)
    TextView textLimitAddPhotos;
    @Nullable
    @BindView(R.id.btnCreateAnnouncement)
    Button btnCreateAnnouncement;
    @Nullable
    @BindView(R.id.containerImg)
    ContainerSelectedImages containerSelectedImages;
    @Nullable
    @BindView(R.id.fieldProductName)
    EditText fieldProductName;
    @Nullable
    @BindView(R.id.fieldDescription)
    EditText fieldProductDescription;
    @Nullable
    @BindView(R.id.fieldCostProduct)
    EditText fieldCostProduct;
    @Nullable
    @BindView(R.id.progressBarInsert)
    ProgressBar progressBarInsert;

    private ContainerFiller containerFiller;
    private SideBar sideBar;
    private Unbinder unbinder;
    private LoadingCategories categories = new LoadingCategories();
    private InsertAnnouncement insertAnnouncement = new InsertAnnouncement();
    private ModelInsertAnnouncement announcement = new ModelInsertAnnouncement();

    public static FragmentInsertAnnouncement getInstance() {
        if (fragmentInsertAnnouncement == null)
            fragmentInsertAnnouncement = new FragmentInsertAnnouncement();

        return fragmentInsertAnnouncement;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_announcement, container, false);

        unbinder = ButterKnife.bind(this, view);

        initializationComponents(view);
        initializationStyles();
        initializationListeners();
        return view;
    }

    private void initializationComponents(View view) {
        containerSelectedImages.setAdapterGalery(this);
        containerSelectedImages.setInstanceCounter(textLimitAddPhotos);

        containerFiller = new ContainerFiller(getContext());
        containerFiller.setContainer(containerSelectedImages);

        categories.loadingCategories(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DropDownList.ModelItemContent>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<DropDownList.ModelItemContent> modelItemContents) {
                        dropDownList.setAdapter(new AdapterRecyclerView(R.layout.drop_down_list_pattern_item,
                                modelItemContents));
                        dropDownList.pushToStack(modelItemContents);
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
    }

    private void initializationStyles() {
        SetBtnStyle.setStyle(new ComponentBackground(getContext(), R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnCreateAnnouncement);

        SetFieldStyle.setEditTextBackground(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), fieldProductName, fieldCostProduct);
        SetFieldStyle.setEditTextBackground(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), fieldProductDescription);

        dropDownList.setBackground(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f));

        dropDownList.setDefaultTitle(getString(R.string.text_category));
        dropDownList.setDefaultError(getResources().getString(R.string.error_please_select_category));
    }

    private void initializationListeners() {
        dropDownList.setOnClickLastElement(v -> setVisibleAdditionalFields(true));

        dropDownList.setOnClickBack(v -> {
            if (!dropDownList.isEmptyBackStack() && dropDownList.CURRENT_SIZE_STACK() > 1) {
                if (dropDownList.isHiden) {
                    dropDownList.expandList();
                } else {
                    dropDownList.popToStack();
                    dropDownList.setTitle(dropDownList.getDefaultTitle());
                    dropDownList.rewriteCollection(dropDownList.getLastElementToStack());
                }
            } else {
                if (dropDownList.isHiden)
                    dropDownList.expandList();
                else
                    dropDownList.hideList();
            }
        });

        dropDownList.setOnClickListener(v -> {
            if (dropDownList.isHiden)
                dropDownList.expandList();
            else {
                dropDownList.hideList();
            }
        });

        fieldCostProduct.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

        btnCreateAnnouncement.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getContext(), fieldProductName, fieldProductDescription, fieldCostProduct)) {
                announcement.setMapBitmap(containerFiller.getMapBitmap());
                announcement.setBitmap(containerFiller.getFirstBitmap());
                announcement.setName(fieldProductName.getText().toString());
                announcement.setIdSubcategory(dropDownList.getIdSelectedElement());
                announcement.setDescription(fieldProductDescription.getText().toString());

                announcement.setCostToBYN(Float.parseFloat(fieldCostProduct.getText().toString()));
                announcement.setCostToUSD(0f);
                announcement.setCostToEUR(0f);

                announcement.setAddress("адрес");

                announcement.setPhone_1("+375(29)659-50-73");
                announcement.setVisiblePhone_1(true);

                announcement.setPhone_2("+375(29)681-37-83");
                announcement.setVisiblePhone_2(false);

                announcement.setPhone_3("+375(44)702-04-50");
                announcement.setVisiblePhone_3(false);

                insertAnnouncement(announcement);
            }
        });
    }

    public void insertAnnouncement(@NonNull ModelInsertAnnouncement announcement) {
        if (announcement != null) {
            progressBarInsert.setVisibility(View.VISIBLE);
            insertAnnouncement.insertAnnouncement(getContext(), ServerUtils.URL_INSERT_ANNOUNCEMENT, announcement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Object o) {
                            Utils.messageOutput(getContext(), getResources().getString(R.string.success_announcement_added));
                            progressBarInsert.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressBarInsert.setVisibility(View.GONE);
                        }

                        @Override
                        public void onComplete() {
                            Utils.messageOutput(getContext(), getResources().getString(R.string.success_announcement_added));
                            progressBarInsert.setVisibility(View.GONE);
                        }
                    });
        }
    }

    public void setVisibleAdditionalFields(boolean b) {
        if (!b) groupAdditionalFields.setVisibility(View.GONE);
        else groupAdditionalFields.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECTED_IMG_CODE && resultCode == getActivity().RESULT_OK) {
            if (resultCode == getActivity().RESULT_OK) {
                containerFiller.setActivityResult(data, getContext());
                containerFiller.inflateContainer();
            }
        }
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_insert_announcement;
    }

    @Override
    public void initComponentsActionBar(ViewGroup viewGroup) {
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemBurgerMenu.setOnClickListener(v -> sideBar.open());
    }

    @Override
    public int getRequestCode() {
        return SELECTED_IMG_CODE;
    }

    @Override
    public AdapterGalery getSelfInstance() {
        return this;
    }

    @Override
    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}