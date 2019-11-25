package com.application.trust.MainWorkspace.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.ContainerImg.ContainerFiller;
import com.application.trust.CustomComponents.ContainerImg.ContainerSelectedImages;
import com.application.trust.CustomComponents.ContainerImg.Galery.AdapterGalery;
import com.application.trust.CustomComponents.DropDownList.DropDownList;
import com.application.trust.CustomComponents.FieldStyle.FieldBackground;
import com.application.trust.CustomComponents.FieldStyle.SetFieldStyle;
import com.application.trust.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.trust.R;
import com.application.trust.ServerInteraction.AddAnnouncement.InflateCategories.AdapterCategory;
import com.application.trust.ServerInteraction.AddAnnouncement.InflateCategories.ItemContent;

import java.util.ArrayList;
import java.util.List;

public class FragmentAddAnnouncement extends Fragment implements AdapterActionBar, AdapterGalery {
    private ImageView itemBack;
    private EditText fieldProductName;
    private DropDownList dropDownList;
    private TextView textLimitAddPhotos;
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
        dropDownList = view.findViewById(R.id.dropDownList);
        fieldProductName = view.findViewById(R.id.fieldProductName);
        containerSelectedImages = view.findViewById(R.id.containerImg);
        textLimitAddPhotos = view.findViewById(R.id.textLimitAddPhotos);

        containerSelectedImages.setAdapterGalery(this);
        containerSelectedImages.setInstanceCounter(textLimitAddPhotos);

        containerFiller = new ContainerFiller(getContext());
        containerFiller.setContainer(containerSelectedImages);

        List<ItemContent> itemContents = new ArrayList<>();
        itemContents.add(new ItemContent("Первый пункт"));
        itemContents.add(new ItemContent("Второй пункт"));
        itemContents.add(new ItemContent("Третий пункт"));
        itemContents.add(new ItemContent("Третий пункт"));
        itemContents.add(new ItemContent("Третий пункт"));
        itemContents.add(new ItemContent("Четвертый пункт"));
        itemContents.add(new ItemContent("Четвертый пункт"));
        itemContents.add(new ItemContent("Четвертый пункт"));

        dropDownList.setAdapterRecyclerView(new AdapterCategory(R.layout.category_pattern_item, itemContents));
//                new InflateDropDownList(getContext()).getListItemContent())
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
                if(dropDownList.isHiden)
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
    public AdapterGalery getSelfInstance() {
        return this;
    }
}