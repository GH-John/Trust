package com.application.arenda.MainWorkspace.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.application.arenda.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.arenda.CustomComponents.Panels.SideBar.AdapterSideBar;
import com.application.arenda.CustomComponents.Panels.SideBar.SideBar;
import com.application.arenda.R;

public class FragmentUserProposals extends Fragment implements AdapterActionBar, AdapterSideBar {
    private ImageView itemBurgerMenu;
    private SideBar sideBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_proposals, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_user_proposals;
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