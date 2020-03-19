package com.application.arenda.MainWorkspace.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.Entities.Announcements.InsertToFavorite.InsertToFavorite;
import com.application.arenda.Entities.Announcements.Models.ModelViewAnnouncement;
import com.application.arenda.Entities.Announcements.ViewAnnouncement.AdapterViewPager;
import com.application.arenda.Entities.Announcements.ViewAnnouncement.IDataViewPager;
import com.application.arenda.Entities.Announcements.ViewAnnouncement.LoadingViewAnnouncement;
import com.application.arenda.Entities.Utils.Network.ServerUtils;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentViewAnnouncement extends Fragment implements AdapterActionBar {

    @Nullable
    @BindView(R.id.dotsLayout)
    LinearLayout dotsLayout;

    @Nullable
    @BindView(R.id.imgViewPager)
    ViewPager imgViewPager;

    @Nullable
    @BindDrawable(R.drawable.preview_dot_selected)
    Drawable dotSelected;

    @Nullable
    @BindDrawable(R.drawable.preview_dot_unselected)
    Drawable dotUnselected;

    @Nullable
    @BindView(R.id.textNameProduct)
    TextView textNameProduct;

    @Nullable
    @BindView(R.id.textCostProduct)
    TextView textCostProduct;

    @Nullable
    @BindView(R.id.textAddress)
    TextView textAddress;

    @Nullable
    @BindView(R.id.textPlacementDate)
    TextView textPlacementDate;

    @Nullable
    @BindView(R.id.textRating)
    TextView textRating;

    @Nullable
    @BindView(R.id.textCountRent)
    TextView textCountRent;

    @Nullable
    @BindView(R.id.textDescriptionProduct)
    TextView textDescriptionProduct;

    @Nullable
    @BindView(R.id.btnInsertToFavorite)
    ImageButton btnInsertToFavorite;

    @Nullable
    @BindView(R.id.progressBarVA)
    ProgressBar progressBar;

    private ImageView itemBtnBack, itemPhone,
            itemMessage, itemMore;

    private Unbinder unbinder;
    private AdapterViewPager adapterViewPager;
    private ViewPager.OnPageChangeListener pageListener;
    private LoadingViewAnnouncement announcement = new LoadingViewAnnouncement();
    private InsertToFavorite favorite = new InsertToFavorite();

    private long idAnnouncement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_announcement, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        if (bundle != null)
            idAnnouncement = bundle.getLong("idAnnouncement");

        loadData();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);

        announcement.loadAnnouncement(getContext(), ServerUtils.URL_LOADING_VIEW_ANNOUNCEMENT, idAnnouncement)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelViewAnnouncement>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ModelViewAnnouncement model) {
                        textPlacementDate.setText(model.getPlacementDate());
                        textNameProduct.setText(model.getName());

                        //будет браться стоимость в зависимости от настроек по умолчанию
                        textCostProduct.setText(model.getCostToBYN() + " руб./ч.");

                        textAddress.setText(model.getAddress());
                        textRating.setText(String.valueOf(model.getRate()));
                        textCountRent.setText(String.valueOf(model.getCountRent()));

                        textDescriptionProduct.setText(model.getDescription());

                        btnInsertToFavorite.setOnClickListener(v -> onClickFavorite(model.getID()));

                        selectHeart(model.isFavorite());

                        setImgViewPager(model.getUriCollection());

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void onClickFavorite(long idAnnouncement) {
        favorite.insertToFavorite(getContext(), ServerUtils.URL_INSERT_TO_FAVORITE,
                idAnnouncement)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isFavorite) {
                        selectHeart(isFavorite);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void selectHeart(boolean b) {
        if (b)
            btnInsertToFavorite.setImageDrawable(getContext().getDrawable(R.drawable.ic_heart_selected));
        else
            btnInsertToFavorite.setImageDrawable(getContext().getDrawable(R.drawable.ic_heart_not_selected));
    }

    private void setImgViewPager(List<Uri> uris) {
        adapterViewPager = new AdapterViewPager(getContext(),
                new IDataViewPager() {
                    @Override
                    public List<Uri> getCollectionUriBitmap() {
                        return uris;
                    }

                    @Override
                    public LinearLayout getDotsLayout() {
                        return dotsLayout;
                    }

                    @Override
                    public Drawable getDotUnselected() {
                        return dotUnselected;
                    }

                    @Override
                    public Drawable getDotSelected() {
                        return dotSelected;
                    }
                });

        imgViewPager.setAdapter(adapterViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_view_announcement;
    }

    @Override
    public void initComponentsActionBar(ViewGroup viewGroup) {
        itemBtnBack = viewGroup.findViewById(R.id.itemBtnBack);
        itemPhone = viewGroup.findViewById(R.id.itemPhone);
        itemMessage = viewGroup.findViewById(R.id.itemMessage);
        itemMore = viewGroup.findViewById(R.id.itemMore);
    }

    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemBtnBack.setOnClickListener(v -> getActivity().onBackPressed());
        itemPhone.setOnClickListener(v -> Utils.messageOutput(getContext(), "phone"));
        itemMessage.setOnClickListener(v -> Utils.messageOutput(getContext(), "message"));
        itemMore.setOnClickListener(v -> Utils.messageOutput(getContext(), "more"));
    }
}