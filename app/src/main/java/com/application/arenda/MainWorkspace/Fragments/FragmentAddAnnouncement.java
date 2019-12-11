package com.application.arenda.MainWorkspace.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.application.arenda.CustomComponents.ComponentBackground;
import com.application.arenda.CustomComponents.ContainerImg.ContainerFiller;
import com.application.arenda.CustomComponents.ContainerImg.ContainerSelectedImages;
import com.application.arenda.CustomComponents.ContainerImg.Galery.AdapterGalery;
import com.application.arenda.CustomComponents.DropDownList.DropDownList;
import com.application.arenda.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.arenda.CustomComponents.Panels.SideBar.AdapterSideBar;
import com.application.arenda.CustomComponents.Panels.SideBar.SideBar;
import com.application.arenda.CustomComponents.SetStyle.SetBtnStyle;
import com.application.arenda.CustomComponents.SetStyle.SetFieldStyle;
import com.application.arenda.Patterns.DecimalDigitsInputFilter;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.InsertAnnouncement.InflateDropDownList.AdapterRecyclerView;
import com.application.arenda.ServerInteraction.InsertAnnouncement.InflateDropDownList.RecipientCategories;
import com.application.arenda.ServerInteraction.InsertAnnouncement.InsertAnnouncement;
import com.application.arenda.ServerInteraction.InsertAnnouncement.ModelAnnouncement;

public class FragmentAddAnnouncement extends Fragment implements AdapterSideBar, AdapterActionBar, AdapterGalery {
    private final int SELECTED_IMG_CODE = 1001;
    private ModelAnnouncement modelAnnouncement;

    private SideBar sideBar;
    private Group groupPhones;
    private ImageView itemBurgerMenu;
    private DropDownList dropDownList;
    private Group groupAdditionalFields;
    private TextView textLimitAddPhotos;
    private Button btnCreateAnnouncement;
    private ContainerFiller containerFiller;
    private ComponentBackground componentBackground;
    private ContainerSelectedImages containerSelectedImages;
    private EditText fieldProductName, fieldProductDescription, fieldCostProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);
        view.setOnTouchListener(this);

        initializationComponents(view);
        initializationStyles();
        initializationListeners();
        return view;
    }

    private void initializationComponents(View view) {
        textLimitAddPhotos = view.findViewById(R.id.textLimitAddPhotos);
        dropDownList = view.findViewById(R.id.dropDownList);
        containerSelectedImages = view.findViewById(R.id.containerImg);

        fieldProductName = view.findViewById(R.id.fieldProductName);
        fieldCostProduct = view.findViewById(R.id.fieldCostProduct);
        fieldProductDescription = view.findViewById(R.id.fieldDescription);

        groupPhones = view.findViewById(R.id.groupPhones);
        groupAdditionalFields = view.findViewById(R.id.groupAdditionalFields);

        btnCreateAnnouncement = view.findViewById(R.id.btnCreateAnnouncement);

        modelAnnouncement = new ModelAnnouncement();

        containerSelectedImages.setAdapterGalery(this);
        containerSelectedImages.setInstanceCounter(textLimitAddPhotos);

        containerFiller = new ContainerFiller(getContext());
        containerFiller.setContainer(containerSelectedImages);

        dropDownList.setAdapter(new AdapterRecyclerView(R.layout.drop_down_list_pattern_item,
                RecipientCategories.getCollectionCategories(getContext(),
                        dropDownList.getProgressBar(), dropDownList)));
    }

    private void initializationStyles() {
        componentBackground = new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f);

        SetBtnStyle.setStyle(new ComponentBackground(getContext(), R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnCreateAnnouncement);

        SetFieldStyle.setEditTextBackground(componentBackground, fieldProductName, fieldCostProduct);
        SetFieldStyle.setEditTextBackground(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), fieldProductDescription);

        dropDownList.setDefaultTitle(getString(R.string.text_category));
    }

    private void initializationListeners() {
        dropDownList.setOnClickLastElement(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibleAdditionalFields(true);
            }
        });

        dropDownList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownList.isHiden)
                    dropDownList.expandList();
                else {
                    dropDownList.hideList();
                }
            }
        });

        fieldCostProduct.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

        btnCreateAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelAnnouncement.setMapBitmap(containerFiller.getMapBitmap());
                modelAnnouncement.setMainBitmap(containerFiller.getFirstBitmap());
                modelAnnouncement.setName(fieldProductName.getText().toString());
                modelAnnouncement.setIdSubcategory(dropDownList.getIdSelectedElement());
                modelAnnouncement.setDescription(fieldProductDescription.getText().toString());

                modelAnnouncement.setCostToBYN(Float.parseFloat(fieldCostProduct.getText().toString()));
                modelAnnouncement.setCostToUSD(0f);
                modelAnnouncement.setCostToEUR(0f);

                modelAnnouncement.setLocation("адрес");

                modelAnnouncement.setPhone_1("+375(29)659-50-73");
                modelAnnouncement.setVisiblePhone_1(true);

                modelAnnouncement.setPhone_2("+375(29)681-37-83");
                modelAnnouncement.setVisiblePhone_2(false);

                modelAnnouncement.setPhone_3("+375(44)702-04-50");
                modelAnnouncement.setVisiblePhone_3(false);

//                ServiceInsertAnnouncement serviceInsertAnnouncement = new ServiceInsertAnnouncement(getContext(), modelAnnouncement);
//
//                Intent service = new Intent(getContext(), serviceInsertAnnouncement.getClass());
//                getActivity().startService(service);

                new InsertAnnouncement(getContext(), modelAnnouncement).execute();
            }
        });
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
        return R.layout.ab_pattern_add_announcement;
    }

    @Override
    public void initializationComponentsActionBar(ViewGroup viewGroup) {
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initializationListenersActionBar(ViewGroup viewGroup) {
        itemBurgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sideBar.expand();
            }
        });
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.sideBar.swipeListener(v, event);
        return true;
    }
}