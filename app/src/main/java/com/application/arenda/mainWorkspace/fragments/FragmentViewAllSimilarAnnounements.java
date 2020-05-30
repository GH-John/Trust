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

public class FragmentViewAllSimilarAnnounements extends Fragment implements AdapterActionBar {

    @BindView(R.id.rvOutputAllSimilarAnnouncements)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Unbinder unbinder;

    private ImageButton itemBtnBack;

    private AllAnnouncementsAdapter similarAnnouncementsAdapter;

    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithRewriteSimilarAnnouncements;
    private SingleObserver<List<ModelAnnouncement>> singleLoaderWithoutRewriteSimilarAnnouncements;

    private ApiAnnouncement api;

    private String userToken;
    private Consumer<List<ModelUser>> consumerUserToken;

    private String searchQuery = null;

    private long idSubcategory;

    private LinearLayoutManager rvLayoutManager;
    private RVOnScrollListener rvOnScrollListener;

    private LocalCacheManager cacheManager;
    private SharedViewModels sharedViewModels;
    private ContainerFragments containerFragments;

    private OnItemClick similarItemClick;
    private OnItemClick similarItemHeartClick;

    private CompositeDisposable disposable = new CompositeDisposable();
    private OnApiListener listenerFavoriteInsert;
    private OnApiListener listenerLoadAnnouncement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_similar_announements, container, false);

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

        setLoadMoreForSimilarAnnouncement();

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

        singleLoaderWithRewriteSimilarAnnouncements = new SingleObserver<List<ModelAnnouncement>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(List<ModelAnnouncement> collection) {
                similarAnnouncementsAdapter.rewriteCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                similarAnnouncementsAdapter.setLoading(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        singleLoaderWithoutRewriteSimilarAnnouncements = new SingleObserver<List<ModelAnnouncement>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(List<ModelAnnouncement> collection) {
                similarAnnouncementsAdapter.addToCollection(collection);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);

                similarAnnouncementsAdapter.setLoading(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        similarItemClick = (viewHolder, model) -> {
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

        similarAnnouncementsAdapter = new AllAnnouncementsAdapter();

        rvOnScrollListener.setRVAdapter(similarAnnouncementsAdapter);

        similarAnnouncementsAdapter.setItemViewClick(similarItemClick);

        similarAnnouncementsAdapter.setItemHeartClick((viewHolder, model) ->
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

        recyclerView.setAdapter(similarAnnouncementsAdapter);
    }

    private void initStyles() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed);

        swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtils.dpToPx(120));
    }

    private void loadData() {
        sharedViewModels.getLastSimilarAnnouncements()
                .observe(getViewLifecycleOwner(), modelAnnouncements -> {
                    idSubcategory = modelAnnouncements.get(0).getIdSubcategory();
                    similarAnnouncementsAdapter.rewriteCollection(modelAnnouncements);
                });
    }

    private void setLoadMoreForSimilarAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> addSimilarAnnouncementsToCollection(lastID, null, false));
    }

    private void setLoadMoreForSearchSimilarAnnouncement() {
        rvOnScrollListener.setOnLoadMoreData(lastID -> searchAnnouncements(searchQuery, lastID));
    }

    private synchronized void addSimilarAnnouncementsToCollection(long lastId, String query, boolean rewrite) {
        if (!similarAnnouncementsAdapter.isLoading()) {
            similarAnnouncementsAdapter.setLoading(true);

            api.loadSimilarAnnouncements(userToken, idSubcategory, lastId, 10, query, listenerLoadAnnouncement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rewrite ? singleLoaderWithRewriteSimilarAnnouncements : singleLoaderWithoutRewriteSimilarAnnouncements);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressLint("CheckResult")
    public void refreshLayout() {
        addSimilarAnnouncementsToCollection(0, null, true);
    }

    @SuppressLint("CheckResult")
    public void searchAnnouncements(String query, long lastId) {
        swipeRefreshLayout.setRefreshing(true);

        addSimilarAnnouncementsToCollection(lastId, query, true);
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_view_all_similar_annoncements;
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