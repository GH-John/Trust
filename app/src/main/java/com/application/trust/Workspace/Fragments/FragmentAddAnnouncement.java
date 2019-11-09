package com.application.trust.Workspace.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.FieldStyle.FieldBackground;
import com.application.trust.CustomComponents.FieldStyle.SetFieldStyle;
import com.application.trust.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.trust.R;

public class FragmentAddAnnouncement extends Fragment implements AdapterActionBar {
    private ImageView itemBack, imgProductCategory;
    private EditText fieldProductName;

    private FieldBackground fieldBackground;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);

        initializationComponents(view);
        initializationStyles();
        initializationListeners();
        return view;
    }

    private void initializationComponents(View view){
        fieldProductName = view.findViewById(R.id.fieldProductName);
        imgProductCategory = view.findViewById(R.id.imgProductCategory);
    }

    private void initializationStyles(){
        fieldBackground = new FieldBackground(this.getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f,
                new float[]{20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});

        SetFieldStyle.setStyle(fieldBackground, fieldProductName);

        imgProductCategory.setImageDrawable(fieldBackground);
    }

    private void initializationListeners(){

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
}