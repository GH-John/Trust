package com.application.arenda.mainWorkspace.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.application.arenda.R;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.sideBar.ItemSideBar;
import com.application.arenda.ui.widgets.sideBar.SideBar;

public final class FragmentUserStatistics extends Fragment implements AdapterActionBar, ItemSideBar {
    @SuppressLint("StaticFieldLeak")
    private static FragmentUserStatistics fragmentUserStatistics;

    private ImageButton itemBurgerMenu;
    private SideBar sideBar;

    public static FragmentUserStatistics getInstance() {
        if (fragmentUserStatistics == null)
            fragmentUserStatistics = new FragmentUserStatistics();
        return fragmentUserStatistics;
    }

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
    public void initComponentsActionBar(ViewGroup viewGroup) {
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemBurgerMenu.setOnClickListener(v -> sideBar.openLeftMenu());
    }

    @Override
    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }
}