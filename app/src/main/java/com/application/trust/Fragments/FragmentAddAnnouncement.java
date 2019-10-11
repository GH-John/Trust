package com.application.trust.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.trust.R;

public class FragmentAddAnnouncement extends Fragment implements AdapterActionBar {
    ImageView itemBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);
        return view;
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_add_announcement;
    }

    @Override
    public void initializeItems(ViewGroup viewGroup) {
        itemBack = viewGroup.findViewById(R.id.itemBack);
    }

    @Override
    public void initializeItemsListener(ViewGroup viewGroup) {
        itemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "back", Toast.LENGTH_LONG).show();
            }
        });
    }
}