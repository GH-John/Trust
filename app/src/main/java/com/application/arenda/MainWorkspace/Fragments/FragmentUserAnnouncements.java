package com.application.arenda.MainWorkspace.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.Entities.Announcements.LoadingAnnouncements.LoadingAnnouncements;
import com.application.arenda.Entities.Announcements.LoadingAnnouncements.UserAnnouncements.UserAnnouncementsAdapter;
import com.application.arenda.Entities.Announcements.Models.ModelUserAnnouncement;
import com.application.arenda.Entities.RecyclerView.RVOnScrollListener;
import com.application.arenda.Entities.Utils.Network.ServerUtils;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;
import com.application.arenda.UI.Components.SideBar.AdapterSideBar;
import com.application.arenda.UI.Components.SideBar.SideBar;
import com.application.arenda.UI.DisplayUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class FragmentUserAnnouncements extends Fragment implements AdapterActionBar, AdapterSideBar {
    @SuppressLint("StaticFieldLeak")
    private static FragmentUserAnnouncements fragmentUserAnnouncements;

    @Nullable
    @BindView(R.id.recyclerViewOutputUserAnnouncements)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Unbinder unbinder;

    private ImageView itemBurgerMenu,
            itemSearch,
            itemFiltr,
            itemClearFieldSearch;

    private SideBar sideBar;
    private EditText itemFieldSearch;
    private TextView itemHeaderName;
    private Group groupSearch, groupDefault;

    private String searchQuery;

    private LoadingAnnouncements loadData;
    private LinearLayoutManager rvLayoutManager;
    private RVOnScrollListener rvOnScrollListener;
    private UserAnnouncementsAdapter userAnnouncementsAdapter;

    public static FragmentUserAnnouncements getInstance() {
        if (fragmentUserAnnouncements == null)
            fragmentUserAnnouncements = new FragmentUserAnnouncements();

        return fragmentUserAnnouncements;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_announcements, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init() {
        loadData = new LoadingAnnouncements(getContext());
        rvLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        initAdapters();
        initStyles();
        initListeners();

        loadListAnnouncement(0);
    }

    private void initAdapters() {
        recyclerView.setLayoutManager(rvLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);

        rvOnScrollListener = new RVOnScrollListener(rvLayoutManager);
//        recyclerView.setOnFlingListener(new RVOnFlingListener(recyclerView));

        recyclerView.addOnScrollListener(rvOnScrollListener);

        userAnnouncementsAdapter = new UserAnnouncementsAdapter();
        rvOnScrollListener.setRVAdapter(userAnnouncementsAdapter);
        recyclerView.setAdapter(userAnnouncementsAdapter);

        userAnnouncementsAdapter.setItemViewClick((viewHolder, model) -> Utils.messageOutput(getContext(), "click"));

        recyclerView.setAdapter(userAnnouncementsAdapter);
    }

    private void initStyles() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed);
    }

    private void initListeners() {
        swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtils.dpToPx(120));

        swipeRefreshLayout.setOnRefreshListener(this::refreshLayout);

        setLoadMoreForUserAnnouncements();
    }

    public void loadListAnnouncement(long lastID) {
        if (!userAnnouncementsAdapter.isLoading()) {
            userAnnouncementsAdapter.setLoading(true);

            loadData.loadUserAnnouncements(lastID, 10, ServerUtils.URL_LOADING_USER_ANNOUNCEMENT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ModelUserAnnouncement>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<ModelUserAnnouncement> collection) {
                            userAnnouncementsAdapter.addToCollection(collection);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            userAnnouncementsAdapter.setLoading(false);
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    });
        }
    }

    public void refreshLayout() {
        if (!userAnnouncementsAdapter.isLoading()) {
            userAnnouncementsAdapter.setLoading(true);

            loadData.loadUserAnnouncements(0, 10, ServerUtils.URL_LOADING_USER_ANNOUNCEMENT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ModelUserAnnouncement>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(List<ModelUserAnnouncement> collection) {
                            userAnnouncementsAdapter.rewriteCollection(collection);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            userAnnouncementsAdapter.setLoading(false);
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    });
        }
    }

    public void searchAnnouncements(String query, long lastID) {
        if (!userAnnouncementsAdapter.isLoading()) {
            userAnnouncementsAdapter.setLoading(true);
            swipeRefreshLayout.setRefreshing(true);

            loadData.searchToUserAnnouncements(lastID, 10, query, ServerUtils.URL_LOADING_USER_ANNOUNCEMENT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ModelUserAnnouncement>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<ModelUserAnnouncement> modelUserAnnouncements) {
                            userAnnouncementsAdapter.rewriteCollection(modelUserAnnouncements);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            userAnnouncementsAdapter.setLoading(false);
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    });
        }
    }

    private void setLoadMoreForUserAnnouncements() {
        rvOnScrollListener.setOnLoadMoreData(this::loadListAnnouncement);
    }

    private void setLoadMoreForSearchAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> searchAnnouncements(searchQuery, lastID));
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_user_announcements;
    }

    @Override
    public void initComponentsActionBar(ViewGroup viewGroup) {
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
    public void initListenersActionBar(final ViewGroup viewGroup) {
        itemFiltr.setOnClickListener(v -> Utils.messageOutput(getContext(), "filtr"));

        itemSearch.setOnClickListener(v -> {
            groupDefault.setVisibility(View.GONE);
            groupSearch.setVisibility(View.VISIBLE);

            itemFieldSearch.requestFocus();
            Utils.showKeyboard(getContext());
        });

        itemClearFieldSearch.setOnClickListener(v -> {
            if (itemFieldSearch.getText().toString().length() > 0)
                itemFieldSearch.setText("");
            else {
                groupSearch.setVisibility(View.GONE);
                groupDefault.setVisibility(View.VISIBLE);
                itemHeaderName.setText(getResources().getString(R.string.ab_title_user_announcements));

                setLoadMoreForUserAnnouncements();

                refreshLayout();
                Utils.closeKeyboard(getContext());
            }
        });

        itemFieldSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                groupSearch.setVisibility(View.GONE);
                groupDefault.setVisibility(View.VISIBLE);

                searchQuery = itemFieldSearch.getText().toString();

                Utils.closeKeyboard(getContext());
                itemFieldSearch.clearFocus();

                if (!searchQuery.isEmpty()) {
                    itemHeaderName.setText(searchQuery);
                    searchAnnouncements(searchQuery, 0);

                    setLoadMoreForSearchAnnouncement();
                } else {
                    itemHeaderName.setText(getResources().getString(R.string.ab_title_user_announcements));
                    refreshLayout();
                }

                return true;
            }
            return false;
        });

        itemBurgerMenu.setOnClickListener(v -> sideBar.open());
    }

    @Override
    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}