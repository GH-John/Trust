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

public class FragmentUserAnnouncements extends Fragment implements AdapterActionBar, AdapterSideBar, View.OnTouchListener {
    private ImageView itemBurgerMenu,
            itemSort;
    private CustomSideBar customSideBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_announcements, container, false);
        return view;
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_user_announcements;
    }

    @Override
    public void initializationComponentsActionBar(ViewGroup viewGroup) {
        itemSort = viewGroup.findViewById(R.id.itemSort);
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initializationListenersActionBar(ViewGroup viewGroup) {
        itemSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "userSort", Toast.LENGTH_LONG).show();
            }
        });

        itemBurgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "userBurger", Toast.LENGTH_LONG).show();
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
        this.customSideBar.swipeListener(v, event);
        return true;
    }
}