package com.application.trust.Workspace.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.trust.R;

public class FragmentUserStatistics extends Fragment implements AdapterActionBar {
    ImageView itemBurgerMenu, itemRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_statistics, container, false);
        return view;
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_user_statistics;
    }

    @Override
    public void initializeItems(ViewGroup viewGroup) {
        itemRefresh = viewGroup.findViewById(R.id.itemRefresh);
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initializeItemsListener(ViewGroup viewGroup) {
        itemRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "refresh", Toast.LENGTH_LONG).show();
            }
        });

        itemBurgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "statBurger", Toast.LENGTH_LONG).show();
            }
        });
    }
}