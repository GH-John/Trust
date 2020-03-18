package com.application.arenda.MainWorkspace.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.Entities.Announcements.InsertToFavorite.InsertToFavorite;
import com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements.AllAnnouncementsAdapter;
import com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements.AllAnnouncementsViewHolder;
import com.application.arenda.Entities.Announcements.LoadingAnnouncements.LoadingAnnouncements;
import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.RecyclerView.RVOnFlingListener;
import com.application.arenda.Entities.RecyclerView.RVOnScrollListener;
import com.application.arenda.Entities.Utils.Network.ServerUtils;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
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

public final class FragmentAllAnnouncements extends Fragment implements AdapterActionBar, AdapterSideBar {
    @SuppressLint("StaticFieldLeak")
    private static FragmentAllAnnouncements fragmentAllAnnouncements;

    @Nullable
    @BindView(R.id.recyclerViewOutputAllAnnouncements)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @BindView(R.id.layoutAllAnnouncements)
    FrameLayout layoutAllAnnouncements;

    private Unbinder unbinder;
    private SideBar sideBar;
    private ImageView itemBurgerMenu,
            itemSearch,
            itemFiltr,
            itemClearFieldSearch;
    private EditText itemFieldSearch;
    private TextView itemHeaderName;
    private Group groupSearch, groupDefault;

    private LinearLayoutManager rvLayoutManager;
    private RVOnScrollListener rvOnScrollListener;
    private AllAnnouncementsAdapter allAnnouncementsAdapter;

    private LoadingAnnouncements loadData = new LoadingAnnouncements();

    private InsertToFavorite insertToFavorite = new InsertToFavorite();

    private String searchQuery;

    public static FragmentAllAnnouncements getInstance() {
        if (fragmentAllAnnouncements == null)
            fragmentAllAnnouncements = new FragmentAllAnnouncements();

        return fragmentAllAnnouncements;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_announcements, container, false);

        unbinder = ButterKnife.bind(this, view);
        rvLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        initAdapters(view);
        initStyles();
        initListeners();

        loadListAnnouncement(0);
        return view;
    }

    private void initAdapters(View view) {
        recyclerView.setLayoutManager(rvLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);

        rvOnScrollListener = new RVOnScrollListener(rvLayoutManager);
        recyclerView.setOnFlingListener(new RVOnFlingListener(recyclerView));
        recyclerView.addOnScrollListener(rvOnScrollListener);

        allAnnouncementsAdapter = new AllAnnouncementsAdapter(getContext());
        rvOnScrollListener.setRVAdapter(allAnnouncementsAdapter);
        recyclerView.setAdapter(allAnnouncementsAdapter);

        allAnnouncementsAdapter.setItemViewClick((viewHolder, model) -> onItemClick(model.getIdAnnouncement()));

        allAnnouncementsAdapter.setItemHeartClick((viewHolder, model) -> insertToFavorite
                .insertToFavorite(getContext(), ServerUtils.URL_INSERT_TO_FAVORITE,
                        model.getIdAnnouncement())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isFavorite) {
                        ((AllAnnouncementsViewHolder) viewHolder).setActiveHeart(isFavorite);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
        recyclerView.setAdapter(allAnnouncementsAdapter);
    }

    private void initStyles() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorBlue,
                R.color.colorRed);
    }

    private void initListeners() {
        swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtils.dpToPx(120));

        swipeRefreshLayout.setOnRefreshListener(this::refreshLayout);

        setLoadMoreForAllAnnouncement();

        allAnnouncementsAdapter.setItemViewClick((viewHolder, model) -> onItemClick(model.getIdAnnouncement()));
    }

    private void setLoadMoreForAllAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(this::loadListAnnouncement);
    }

    private void setLoadMoreForSearchAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> searchAnnouncements(searchQuery, lastID));
    }

    public void loadListAnnouncement(long lastID) {
        if (!allAnnouncementsAdapter.isLoading()) {
            allAnnouncementsAdapter.setLoading(true);

            loadData.loadAllAnnouncements(getContext(), lastID, ServerUtils.URL_LOADING_ALL_ANNOUNCEMENT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ModelAllAnnouncement>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<ModelAllAnnouncement> collection) {
                            allAnnouncementsAdapter.addToCollection(collection);

                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }

                        @Override
                        public void onComplete() {
                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }
                    });
        }
    }

    public void refreshLayout() {
        if (!allAnnouncementsAdapter.isLoading()) {
            allAnnouncementsAdapter.setLoading(true);

            loadData.loadAllAnnouncements(getContext(), 0, ServerUtils.URL_LOADING_ALL_ANNOUNCEMENT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ModelAllAnnouncement>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(List<ModelAllAnnouncement> collection) {
                            allAnnouncementsAdapter.rewriteCollection(collection);

                            allAnnouncementsAdapter.setLoading(false);
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }

                        @Override
                        public void onComplete() {
                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }
                    });
        }
    }

    private void onItemClick(long idAnnouncement) {
        FragmentViewAnnouncement announcement = new FragmentViewAnnouncement();
        Bundle bundle = new Bundle();
        bundle.putLong("idAnnouncement", idAnnouncement);
        announcement.setArguments(bundle);

        ContainerFragments.getInstance().replaceFragmentInContainer(announcement);
    }

    public void searchAnnouncements(String query, long lastId) {
        if (!allAnnouncementsAdapter.isLoading()) {
            allAnnouncementsAdapter.setLoading(true);
            swipeRefreshLayout.setRefreshing(true);

            loadData.searchToAllAnnouncements(getContext(), ServerUtils.URL_LOADING_ALL_ANNOUNCEMENT, lastId, query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ModelAllAnnouncement>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<ModelAllAnnouncement> modelAllAnnouncements) {
                            allAnnouncementsAdapter.rewriteCollection(modelAllAnnouncements);

                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }

                        @Override
                        public void onComplete() {
                            swipeRefreshLayout.setRefreshing(false);
                            allAnnouncementsAdapter.setLoading(false);
                        }
                    });
        }
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_all_announcements;
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
        itemFiltr.setOnClickListener(v -> Toast.makeText(getContext(), "filter", Toast.LENGTH_LONG).show());

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
                itemHeaderName.setText(getResources().getString(R.string.ab_title_all_announcements));

                setLoadMoreForAllAnnouncement();

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
                    itemHeaderName.setText(getResources().getString(R.string.ab_title_all_announcements));
                    refreshLayout();
                }

                return true;
            }
            return false;
        });

        itemBurgerMenu.setOnClickListener(v -> sideBar.expand());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}