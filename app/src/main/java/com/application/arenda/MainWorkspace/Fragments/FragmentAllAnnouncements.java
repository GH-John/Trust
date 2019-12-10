package com.application.arenda.MainWorkspace.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.CustomComponents.Panels.ActionBar.AdapterActionBar;
import com.application.arenda.CustomComponents.Panels.SideBar.AdapterSideBar;
import com.application.arenda.CustomComponents.Panels.SideBar.SideBar;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.LoadingAnnouncements.LoadingAnnouncements;

public class FragmentAllAnnouncements extends Fragment implements AdapterActionBar, AdapterSideBar {
    private SideBar sideBar;
    private ImageView itemBurgerMenu,
            itemSearch,
            itemSort;
    private RecyclerView recyclerView;
    private LoadingAnnouncements loadingAnnouncements;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_announcements, container, false);
        view.setOnTouchListener(this);
        initializationComponents(view);
        return view;
    }

    private void initializationComponents(View view){
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerViewOutputAnnouncements);

        new LoadingAnnouncements(getContext(), R.layout.template_announcement, recyclerView, swipeRefreshLayout);
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_all_announcements;
    }

    @Override
    public void initializationComponentsActionBar(ViewGroup viewGroup) {
        itemSort = viewGroup.findViewById(R.id.itemSort);
        itemSearch = viewGroup.findViewById(R.id.itemSearch);
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initializationListenersActionBar(final ViewGroup viewGroup) {
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