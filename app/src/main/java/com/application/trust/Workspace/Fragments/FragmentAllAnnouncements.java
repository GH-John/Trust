package com.application.trust.Workspace.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.trust.CustomComponents.Panels.SideBar.AdapterSideBar;
import com.application.trust.CustomComponents.Panels.SideBar.CustomSideBar;
import com.application.trust.R;

public class FragmentAllAnnouncements extends Fragment implements View.OnTouchListener, AdapterActionBar, AdapterSideBar {
    private CustomSideBar customSideBar;
    private ImageView itemBurgerMenu,
            itemSearch,
            itemSort;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_announcements, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_all_announcements;
    }

    @Override
    public void initializeItems(ViewGroup viewGroup) {
        itemSort = viewGroup.findViewById(R.id.itemSort);
        itemSearch = viewGroup.findViewById(R.id.itemSearch);
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initializeItemsListener(final ViewGroup viewGroup) {
        itemSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "allSort", Toast.LENGTH_LONG).show();
            }
        });

        itemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "allSearch", Toast.LENGTH_LONG).show();
            }
        });

        itemBurgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customSideBar.expand();
            }
        });
    }

    @Override
    public void setCustomSideBar(CustomSideBar sideBar) {
        this.customSideBar = sideBar;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        customSideBar.swipeListener(v, event);
        return true;
    }
}