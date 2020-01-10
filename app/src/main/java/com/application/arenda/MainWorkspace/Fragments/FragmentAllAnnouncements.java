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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements.AllAnnouncementsRV;
import com.application.arenda.Entities.Announcements.LoadingAnnouncements.LoadingAnnouncements;
import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Utils.ServerUtils;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
import com.application.arenda.UI.Components.SideBar.AdapterSideBar;
import com.application.arenda.UI.Components.SideBar.SideBar;

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

    private Unbinder unbinder;

    private SideBar sideBar;
    private ImageView itemBurgerMenu,
            itemSearch,
            itemFiltr,
            itemClearFieldSearch;

    private EditText itemFieldSearch;
    private TextView itemHeaderName;
    private Group groupSearch, groupDefault;
    private AllAnnouncementsRV allAnnouncementsRV;

    private LoadingAnnouncements load = new LoadingAnnouncements();

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

        initializationComponents(view);
        initializationStyles();
        initializationListeners();
        return view;
    }

    private void initializationComponents(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setHasFixedSize(true);

        loadLayout();
    }

    private void initializationStyles() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorBlue,
                R.color.colorRed);
    }

    private void initializationListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::refreshLayout);
    }

    public void loadLayout() {
        swipeRefreshLayout.setRefreshing(true);
        load.loadAllAnnouncements(getContext(), ServerUtils.URL_LOADING_ALL_ANNOUNCEMENT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ModelAllAnnouncement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ModelAllAnnouncement> modelAllAnnouncements) {
                        allAnnouncementsRV = new AllAnnouncementsRV(getContext(),
                                R.layout.template_1_announcement, modelAllAnnouncements);

                        allAnnouncementsRV.onItemClick(model -> onItemClick(model.getIdAnnouncement()));

                        recyclerView.setAdapter(allAnnouncementsRV);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public void refreshLayout() {
        load.loadAllAnnouncements(getContext(), ServerUtils.URL_LOADING_ALL_ANNOUNCEMENT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ModelAllAnnouncement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<ModelAllAnnouncement> modelAllAnnouncements) {
                        allAnnouncementsRV.rewriteCollection(modelAllAnnouncements);

                        allAnnouncementsRV.onItemClick(model -> onItemClick(model.getIdAnnouncement()));

                        recyclerView.setAdapter(allAnnouncementsRV);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void onItemClick(int idAnnouncement) {
        FragmentViewAnnouncement announcement = new FragmentViewAnnouncement();
        Bundle bundle = new Bundle();
        bundle.putInt("idAnnouncement", idAnnouncement);
        announcement.setArguments(bundle);

        ContainerFragments.getInstance().replaceFragmentInContainer(announcement);
    }

    public void searchAnnouncements(String s) {
        swipeRefreshLayout.setRefreshing(true);

        load.searchToAllAnnouncements(getContext(), ServerUtils.URL_LOADING_ALL_ANNOUNCEMENT, 0, s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ModelAllAnnouncement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<ModelAllAnnouncement> modelAllAnnouncements) {
                        allAnnouncementsRV.rewriteCollection(modelAllAnnouncements);

                        recyclerView.setAdapter(allAnnouncementsRV);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
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

                refreshLayout();
                Utils.closeKeyboard(getContext());
            }
        });

        itemFieldSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                groupSearch.setVisibility(View.GONE);
                groupDefault.setVisibility(View.VISIBLE);

                String search = itemFieldSearch.getText().toString();

                Utils.closeKeyboard(getContext());
                itemFieldSearch.clearFocus();

                if (!search.isEmpty()) {
                    itemHeaderName.setText(search);
                    searchAnnouncements(search);
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