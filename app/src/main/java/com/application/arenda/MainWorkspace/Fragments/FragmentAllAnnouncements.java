package com.application.arenda.MainWorkspace.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.UI.Panels.ActionBar.AdapterActionBar;
import com.application.arenda.UI.Panels.SideBar.AdapterSideBar;
import com.application.arenda.UI.Panels.SideBar.SideBar;
import com.application.arenda.Patterns.Utils;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.LoadingAnnouncements.LoadingAnnouncements;

public class FragmentAllAnnouncements extends Fragment implements AdapterActionBar, AdapterSideBar {
    private SideBar sideBar;

    private ImageView itemBurgerMenu,
            itemSearch,
            itemFiltr,
            itemClearFieldSearch;

    private EditText itemFieldSearch;
    private TextView itemHeaderName;

    private Group groupSearch, groupDefault;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadingAnnouncements loadingAnnouncements;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_announcements, container, false);
        view.setOnTouchListener(this);
        initializationComponents(view);
        return view;
    }

    private void initializationComponents(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerViewOutputAnnouncements);

        loadingAnnouncements = new LoadingAnnouncements(getContext(), R.layout.template_2_announcement,
                R.layout.template_progress_bar, recyclerView, swipeRefreshLayout);
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_all_announcements;
    }

    @Override
    public void initializationComponentsActionBar(ViewGroup viewGroup) {
        itemFiltr = viewGroup.findViewById(R.id.itemFiltr);
        itemSearch = viewGroup.findViewById(R.id.itemSearch);
        itemHeaderName = viewGroup.findViewById(R.id.itemHeaderName);
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
        itemFieldSearch = viewGroup.findViewById(R.id.itemFieldSearch);
        itemClearFieldSearch = viewGroup.findViewById(R.id.itemClearFieldSearch);

        groupSearch = viewGroup.findViewById(R.id.groupSearch);
        groupDefault = viewGroup.findViewById(R.id.groupDefault);
    }

    @Override
    public void initializationListenersActionBar(final ViewGroup viewGroup) {
        itemFiltr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "allSort", Toast.LENGTH_LONG).show();
            }
        });

        itemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupDefault.setVisibility(View.GONE);
                groupSearch.setVisibility(View.VISIBLE);

                Utils.showKeyboard(getContext());
            }
        });

        itemClearFieldSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemFieldSearch.getText().toString().length() > 0)
                    itemFieldSearch.setText("");
                else {
                    groupSearch.setVisibility(View.GONE);
                    groupDefault.setVisibility(View.VISIBLE);

                    Utils.closeKeyboard(getContext());
                }
            }
        });

        itemFieldSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    groupSearch.setVisibility(View.GONE);
                    groupDefault.setVisibility(View.VISIBLE);

                    String search = itemFieldSearch.getText().toString();

                    Utils.closeKeyboard(getContext());
                    itemFieldSearch.clearFocus();

                    if (!search.isEmpty())
                        itemHeaderName.setText(search);
                    else
                        itemHeaderName.setText(getResources().getString(R.string.ab_title_all_announcements));

                    loadingAnnouncements.search(search);
                    return true;
                }
                return false;
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
        return false;
    }
}