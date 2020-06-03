package com.application.arenda.mainWorkspace.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.entities.serverApi.announcement.ApiAnnouncement;
import com.application.arenda.entities.announcements.loadingAnnouncements.all.AllAnnouncementsAdapter;
import com.application.arenda.entities.announcements.loadingAnnouncements.all.AllAnnouncementsVH;
import com.application.arenda.entities.serverApi.OnApiListener;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.models.SharedViewModels;
import com.application.arenda.entities.recyclerView.OnItemClick;
import com.application.arenda.entities.recyclerView.RVOnScrollListener;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.serverApi.client.CodeHandler;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.R;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments;
import com.application.arenda.entities.utils.DisplayUtils;

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

public class FragmentViewAllLandLordAnnouncements extends Fragment implements AdapterActionBar {

    @BindView(R.id.rvOutputAllLandLordAnnouncements)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Unbinder unbinder;

    private ImageButton itemBtnBack;

    private AllAnnouncementsAdapter allAnnouncementsAdapter;

    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithRewriteAnnouncements;
    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithoutRewriteAnnouncements;

    private ApiAnnouncement api;

    private String userToken;
    private Consumer<List<ModelUser>> consumerUserToken;

    private String searchQuery = null;

    private long idLandLord;

    private LinearLayoutManager rvLayoutManager;
    private RVOnScrollListener rvOnScrollListener;

    private LocalCacheManager cacheManager;
    private SharedViewModels sharedViewModels;
    private ContainerFragments containerFragments;

    private OnItemClick landLordItemClick;
    private OnItemClick landLordItemHeartClick;

    private CompositeDisposable disposable = new CompositeDisposable();
    private OnApiListener listenerFavoriteInsert;
    private OnApiListener listenerLoadAnnouncement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_land_lord_announcements, container, false);

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

        setLoadMoreForAllAnnouncement();

        loadData();
    }

    @SuppressLint("CheckResult")
    private void initInterfaces() {
        swipeRefreshLayout.setOnRefreshListener(this::refreshLayout);

        listenerFavoriteInsert = new OnApiListener() {
            @Override
            public void onComplete(@NonNull CodeHandler code) {
                if (code.equals(CodeHandler.USER_NOT_FOUND)) {
                    Utils.messageOutput(getContext(), getResources().getString(R.string.warning_login_required));
                } else if(code.equals(CodeHandler.INTERNAL_SERVER_ERROR)){
                    Utils.messageOutput(getContext(), getResources().getString(R.string.error_on_server));
                }
                else if (code.equals(CodeHandler.UNKNOW_ERROR)) {
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

            }

            @Override
            public void onFailure(@NonNull Throwable t) {

            }
        };

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

        singleLoaderWithRewriteAnnouncements = new SingleObserver<List<ModelAnnouncement>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(List<ModelAnnouncement> collection) {
                allAnnouncementsAdapter.rewriteCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                allAnnouncementsAdapter.setLoading(false);
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
                allAnnouncementsAdapter.addToCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                allAnnouncementsAdapter.setLoading(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        landLordItemClick = (viewHolder, model, position) -> {

            sharedViewModels.selectAnnouncement((ModelAnnouncement) model);

            containerFragments.open(new FragmentViewAnnouncement());
        };
    }

    @SuppressLint("CheckResult")
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

        allAnnouncementsAdapter = new AllAnnouncementsAdapter();

        rvOnScrollListener.setRVAdapter(allAnnouncementsAdapter);

        allAnnouncementsAdapter.setItemViewClick(landLordItemClick);

        allAnnouncementsAdapter.setItemHeartClick((viewHolder, model, position) ->
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

        recyclerView.setAdapter(allAnnouncementsAdapter);
    }

    private void initStyles() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed);

        swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtils.dpToPx(120));
    }

    private void loadData() {
        sharedViewModels.getLastLandLordAnnouncements()
                .observe(getViewLifecycleOwner(), modelAnnouncements -> {
                    idLandLord = modelAnnouncements.get(0).getIdUser();
                    allAnnouncementsAdapter.rewriteCollection(modelAnnouncements);
                });
    }

    private void setLoadMoreForAllAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> addAnnouncementsToCollection(lastID, null, false));
    }

    private void setLoadMoreForSearchAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> searchAnnouncements(searchQuery, lastID));
    }

    private synchronized void addAnnouncementsToCollection(long lastId, String query, boolean rewrite) {
        if (!allAnnouncementsAdapter.isLoading()) {
            allAnnouncementsAdapter.setLoading(true);

            api.loadLandLordAnnouncements(userToken, idLandLord, lastId, 10, query, listenerLoadAnnouncement)
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
    public void searchAnnouncements(String query, long lastId) {
        swipeRefreshLayout.setRefreshing(true);

        addAnnouncementsToCollection(lastId, query, true);
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_view_all_land_lord_announcements;
    }

    @Override
    public void initComponentsActionBar(ViewGroup viewGroup) {
        itemBtnBack = viewGroup.findViewById(R.id.itemBtnBack);
    }

    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemBtnBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}