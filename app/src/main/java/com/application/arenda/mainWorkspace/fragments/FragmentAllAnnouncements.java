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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.R;
import com.application.arenda.entities.announcements.loadingAnnouncements.all.AllAnnouncementsAdapter;
import com.application.arenda.entities.announcements.loadingAnnouncements.all.AllAnnouncementsVH;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.models.SharedViewModels;
import com.application.arenda.entities.recyclerView.RVOnScrollListener;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.serverApi.OnApiListener;
import com.application.arenda.entities.serverApi.announcement.ApiAnnouncement;
import com.application.arenda.entities.serverApi.client.CodeHandler;
import com.application.arenda.entities.utils.DisplayUtils;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments;
import com.application.arenda.ui.widgets.sideBar.ItemSideBar;
import com.application.arenda.ui.widgets.sideBar.SideBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class FragmentAllAnnouncements extends Fragment implements AdapterActionBar, ItemSideBar {
    @SuppressLint("StaticFieldLeak")
    private static FragmentAllAnnouncements fragmentAllAnnouncements;

    @Nullable
    @BindView(R.id.rvOutputAllAnnouncements)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

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
    private AllAnnouncementsAdapter rvAdapter;

    private ApiAnnouncement api;

    private String userToken = null;

    private String searchQuery = null;

    private ContainerFragments containerFragments;
    private LocalCacheManager cacheManager;

    private CompositeDisposable disposable = new CompositeDisposable();

    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithRewriteAnnouncements;
    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithoutRewriteAnnouncements;

    private Consumer<List<ModelUser>> consumerUserToken;
    private OnApiListener listenerFavoriteInsert;
    private OnApiListener listenerLoadAnnouncement;

    private SharedViewModels sharedViewModels;

    private FragmentAllAnnouncements() {
    }

    public static FragmentAllAnnouncements getInstance() {
        if (fragmentAllAnnouncements == null)
            fragmentAllAnnouncements = new FragmentAllAnnouncements();

        return fragmentAllAnnouncements;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_announcements, container, false);

        unbinder = ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init() {
        api = ApiAnnouncement.getInstance(getContext());
        cacheManager = LocalCacheManager.getInstance(getContext());

        rvLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        containerFragments = ContainerFragments.getInstance(getContext());

        sharedViewModels = new ViewModelProvider(requireActivity()).get(SharedViewModels.class);

        initInterfaces();
        initAdapters();
        initStyles();
        initListeners();
    }

    @SuppressLint("CheckResult")
    private void initInterfaces() {
        listenerFavoriteInsert = new OnApiListener() {
            @Override
            public void onComplete(@NonNull CodeHandler code) {
                if (code.equals(CodeHandler.USER_NOT_FOUND)) {
                    Utils.messageOutput(getContext(), getResources().getString(R.string.warning_login_required));
                } else if (code.equals(CodeHandler.INTERNAL_SERVER_ERROR)) {
                    Utils.messageOutput(getContext(), getResources().getString(R.string.error_on_server));
                } else if (code.equals(CodeHandler.UNKNOW_ERROR)) {
                    Utils.messageOutput(getContext(), getResources().getString(R.string.unknown_error));
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Timber.e(t);
            }
        };

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
                        break;
                    }

//                    case NONE_REZULT: {
//                        Utils.messageOutput(getContext(), "Нет объявлений");
//                        break;
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Timber.e(t);
            }
        };

        consumerUserToken = modelUsers -> {
            if (modelUsers.size() > 0)
                userToken = modelUsers.get(0).getToken();
            else
                userToken = null;

            swipeRefreshLayout.setRefreshing(true);
            refreshLayout();
        };

        cacheManager
                .users()
                .getActiveUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserToken);

        singleLoaderWithRewriteAnnouncements = new SingleObserver<List<ModelAnnouncement>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(List<ModelAnnouncement> collection) {
                rvAdapter.rewriteCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                rvAdapter.setLoading(false);
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
                rvAdapter.addToCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                rvAdapter.setLoading(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    @SuppressLint("CheckResult")
    private void initAdapters() {
        recyclerView.setLayoutManager(rvLayoutManager);

        recyclerView.setItemViewCacheSize(50);
        recyclerView.setHasFixedSize(true);

        rvOnScrollListener = new RVOnScrollListener(rvLayoutManager);

        recyclerView.addOnScrollListener(rvOnScrollListener);

        rvAdapter = new AllAnnouncementsAdapter();

        rvOnScrollListener.setRVAdapter(rvAdapter);

        rvAdapter.setItemViewClick((viewHolder, model, pos) -> {

            sharedViewModels.selectAnnouncement((ModelAnnouncement) model);

            containerFragments.open(new FragmentViewAnnouncement());
        });

        rvAdapter.setItemHeartClick((viewHolder, model, pos) ->
                api.insertToFavorite(userToken, model.getID(), listenerFavoriteInsert)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable.add(d);
                            }

                            @Override
                            public void onSuccess(Boolean isFavorite) {
                                ((AllAnnouncementsVH) viewHolder).setActiveHeart(isFavorite);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                            }
                        }));

        rvAdapter.setItemUserAvatarClick((viewHolder, model, pos) -> {
            sharedViewModels.selectUser(((ModelAnnouncement) model).getIdUser());
            containerFragments.open(FragmentViewerUserProfile.Companion.getInstance());
        });

        recyclerView.setAdapter(rvAdapter);
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

        setLoadMoreForAllAnnouncement();
    }

    private void setLoadMoreForAllAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> addAnnouncementsToCollection(lastID, null, false));
    }

    private void setLoadMoreForSearchAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> searchAnnouncements(searchQuery, lastID, false));
    }

    private synchronized void addAnnouncementsToCollection(long lastId, String query, boolean rewrite) {
        if (!rvAdapter.isLoading()) {
            rvAdapter.setLoading(true);

            api.loadAnnouncements(userToken, lastId, 10, query, listenerLoadAnnouncement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
    public void searchAnnouncements(String query, long lastId, boolean rewrite) {
        addAnnouncementsToCollection(lastId, query, rewrite);
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
        itemFiltr.setOnClickListener(v -> sideBar.openRightMenu());

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
                    searchAnnouncements(searchQuery, 0, true);

                    setLoadMoreForSearchAnnouncement();
                } else {
                    itemHeaderName.setText(getResources().getString(R.string.ab_title_all_announcements));
                    refreshLayout();
                }

                return true;
            }
            return false;
        });

        itemBurgerMenu.setOnClickListener(v -> sideBar.openLeftMenu());
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.clear();
    }

    @Override
    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}