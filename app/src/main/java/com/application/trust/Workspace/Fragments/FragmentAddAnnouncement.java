package com.application.trust.Workspace.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.ContainerImg.ContainerFiller;
import com.application.trust.CustomComponents.ContainerImg.ContainerSelectedImages;
import com.application.trust.CustomComponents.ContainerImg.IGalery;
import com.application.trust.CustomComponents.FieldStyle.FieldBackground;
import com.application.trust.CustomComponents.FieldStyle.SetFieldStyle;
import com.application.trust.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.trust.R;

public class FragmentAddAnnouncement extends Fragment implements AdapterActionBar, IGalery {
    private EditText fieldProductName;
    private ImageView itemBack, imgProductCategory;
    private ContainerFiller containerFiller;
    private ContainerSelectedImages containerSelectedImages;

    private FieldBackground fieldBackground;
    private final int SELECTED_IMG_CODE = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);

        initializationComponents(view);
        initializationStyles();
        initializationListeners();
        return view;
    }

    private void initializationComponents(View view) {
        fieldProductName = view.findViewById(R.id.fieldProductName);
        imgProductCategory = view.findViewById(R.id.imgProductCategory);
        containerSelectedImages = view.findViewById(R.id.containerImg);

        containerFiller = new ContainerFiller(getContext());
        containerSelectedImages.setInstanceGalery(this);
        containerSelectedImages.setContainerFiller(containerFiller);
    }

    private void initializationStyles() {
        fieldBackground = new FieldBackground(this.getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetFieldStyle.setEditTextBackground(fieldBackground, fieldProductName);

        imgProductCategory.setImageDrawable(fieldBackground);
    }

    private void initializationListeners() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECTED_IMG_CODE && resultCode == getActivity().RESULT_OK) {
            if (resultCode == getActivity().RESULT_OK) {
                containerFiller.setContainer(containerSelectedImages);
                containerFiller.inflateContainer(data, getContext());
            }
        }
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_add_announcement;
    }

    @Override
    public void initializationComponentsActionBar(ViewGroup viewGroup) {
        itemBack = viewGroup.findViewById(R.id.itemBack);
    }

    @Override
    public void initializationListenersActionBar(ViewGroup viewGroup) {
        itemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "back", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getRequestCode() {
        return SELECTED_IMG_CODE;
    }

    @Override
    public IGalery getInstance() {
        return this;
    }
}