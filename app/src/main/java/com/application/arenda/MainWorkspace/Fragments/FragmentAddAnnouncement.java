package com.application.arenda.MainWorkspace.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.arenda.CustomComponents.ContainerImg.ContainerFiller;
import com.application.arenda.CustomComponents.ContainerImg.ContainerSelectedImages;
import com.application.arenda.CustomComponents.ContainerImg.Galery.AdapterGalery;
import com.application.arenda.CustomComponents.DropDownList.DropDownList;
import com.application.arenda.CustomComponents.FieldStyle.FieldBackground;
import com.application.arenda.CustomComponents.FieldStyle.SetFieldStyle;
import com.application.arenda.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.arenda.CustomComponents.Panels.SideBar.AdapterSideBar;
import com.application.arenda.CustomComponents.Panels.SideBar.SideBar;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories.AdapterCategory;
import com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories.InflateDropDownList;

public class FragmentAddAnnouncement extends Fragment implements AdapterSideBar, AdapterActionBar, AdapterGalery {
    private final int SELECTED_IMG_CODE = 1001;
    private ImageView itemBurgerMenu;
    private DropDownList dropDownList;
    private EditText fieldProductName;
    private TextView textLimitAddPhotos;
    private ContainerFiller containerFiller;
    private FieldBackground fieldBackground;
    private ContainerSelectedImages containerSelectedImages;
    private SideBar sideBar;

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
        dropDownList = view.findViewById(R.id.dropDownList);
        fieldProductName = view.findViewById(R.id.fieldProductName);
        containerSelectedImages = view.findViewById(R.id.containerImg);
        textLimitAddPhotos = view.findViewById(R.id.textLimitAddPhotos);

        containerSelectedImages.setAdapterGalery(this);
        containerSelectedImages.setInstanceCounter(textLimitAddPhotos);

        containerFiller = new ContainerFiller(getContext());
        containerFiller.setContainer(containerSelectedImages);

        dropDownList.setAdapter(new AdapterCategory(R.layout.category_pattern_item,
                InflateDropDownList.getCollectionCategories(getContext(),
                        dropDownList.getProgressBar(), dropDownList)));
    }

    private void initializationStyles() {
        fieldBackground = new FieldBackground(this.getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetFieldStyle.setEditTextBackground(fieldBackground, fieldProductName);

        dropDownList.setTitle(getString(R.string.text_category));
    }

    private void initializationListeners() {
        dropDownList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownList.isHiden)
                    dropDownList.expandList();
                else
                    dropDownList.hideList();
            }
        });
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