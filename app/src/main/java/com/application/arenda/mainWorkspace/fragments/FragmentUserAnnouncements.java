package com.application.arenda.mainWorkspace.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.entities.announcements.ApiAnnouncement;
import com.application.arenda.entities.announcements.loadingAnnouncements.UserAnnouncements.UserAnnouncementsAdapter;
import com.application.arenda.entities.announcements.OnApiListener;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.recyclerView.RVOnScrollListener;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.utils.retrofit.CodeHandler;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.R;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.sideBar.ItemSideBar;
import com.application.arenda.ui.widgets.sideBar.SideBar;
import com.application.arenda.ui.DisplayUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class FragmentUserAnnouncements extends Fragment implements AdapterActionBar, ItemSideBar {
    @SuppressLint("StaticFieldLeak")
    private static FragmentUserAnnouncements instance;

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

    private String userToken = null;
    private String searchQuery = null;

    private ApiAnnouncement api;

    private LocalCacheManager cacheManager;

    private CompositeDisposable disposable = new CompositeDisposable();
    private DisposableMaybeObserver<ModelUser> maybeWithRewriteCollection;

    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithRewriteAnnouncements;
    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithoutRewriteAnnouncements;

    private DisposableMaybeObserver<ModelUser> maybeWithoutRewriteCollection;
    private Consumer<List<ModelUser>> consumerUserToken;

    private LinearLayoutManager rvLayoutManager;
    private RVOnScrollListener rvOnScrollListener;
    private UserAnnouncementsAdapter userAnnouncementsAdapter;

    private OnApiListener listenerLoadAnnouncement;

    private FragmentUserAnnouncements() {
    }

    public static FragmentUserAnnouncements getInstance() {
        if (instance == null)
            instance = new FragmentUserAnnouncements();

        return instance;
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
        api = ApiAnnouncement.getInstance();
        cacheManager = LocalCacheManager.getInstance(getContext());
        rvLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        initInterfaces();

        initAdapters();
        initStyles();
        initListeners();

        refreshLayout();
    }

    @SuppressLint("CheckResult")
    private void initInterfaces() {
        consumerUserToken = modelUsers -> {
            if (modelUsers.size() > 0)
                userToken = modelUsers.get(0).getToken();
            else
                userToken = null;
        };

        cacheManager
                .users()
                .getActiveUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserToken);

        listenerLoadAnnouncement = new OnApiListener() {
            @Override
            public void onComplete(@NonNull CodeHandler code) {
                switch (code) {
                    case UNKNOW_ERROR:
                    case UNSUCCESS:
                    case NOT_CONNECT_TO_DB:
                    case HTTP_NOT_FOUND:
                    case NETWORK_ERROR: {
                        Utils.messageOutput(getContext(), getResources().getString(R.string.error_check_internet_connect));
                    }

                    case NONE_REZULT: {
                        Utils.messageOutput(getContext(), "Нет объявлений");
                        break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Timber.e(t);
            }
        };

        singleLoaderWithRewriteAnnouncements = new SingleObserver<List<ModelAnnouncement>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(List<ModelAnnouncement> collection) {
                userAnnouncementsAdapter.rewriteCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                userAnnouncementsAdapter.setLoading(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        singleLoaderWithoutRewriteAnnouncements = new SingleObserver<List<ModelAnnouncement>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(List<ModelAnnouncement> collection) {
                userAnnouncementsAdapter.addToCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                userAnnouncementsAdapter.setLoading(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        maybeWithRewriteCollection = new DisposableMaybeObserver<ModelUser>() {
            @Override
            public void onSuccess(ModelUser user) {
                addAnnouncementsToCollection(0, null, true);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }

            @Override
            public void onComplete() {
                addAnnouncementsToCollection(0, null, true);
            }
        };

        maybeWithoutRewriteCollection = new DisposableMaybeObserver<ModelUser>() {
            @Override
            public void onSuccess(ModelUser user) {
                addAnnouncementsToCollection(0, null, false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }

            @Override
            public void onComplete() {
                addAnnouncementsToCollection(0, null, false);
            }
        };
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

    private void setLoadMoreForUserAnnouncements() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> addAnnouncementsToCollection(lastID, null, false));
    }

    private void setLoadMoreForSearchAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> searchAnnouncements(searchQuery, lastID));
    }

    private synchronized void addAnnouncementsToCollection(long lastId, String query, boolean rewrite) {
        if (!userAnnouncementsAdapter.isLoading()) {
            userAnnouncementsAdapter.setLoading(true);

            api.loadUserAnnouncements(getContext(), userToken, lastId, 10, query, listenerLoadAnnouncement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribe(rewrite ? singleLoaderWithRewriteAnnouncements : singleLoaderWithoutRewriteAnnouncements);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressLint("CheckResult")
    public void refreshLayout() {
        addAnnouncementsToCollection(0, null, true);
    }

    @SuppressLint("CheckResult")
    public void searchAnnouncements(String query, long lastId) {
        swipeRefreshLayout.setRefreshing(true);

        addAnnouncementsToCollection(lastId, query, true);
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

        itemBurgerMenu.setOnClickListener(v -> sideBar.openLeftMenu());
    }

    @Override
    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}