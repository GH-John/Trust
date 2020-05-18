package com.application.arenda.mainWorkspace.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.entities.announcements.ApiAnnouncement;
import com.application.arenda.entities.announcements.loadingAnnouncements.ViewAnnouncement.LandLord_Similar_AnnouncementsAdapter;
import com.application.arenda.entities.announcements.loadingAnnouncements.ViewAnnouncement.LandLord_Similar_AnnouncementsVH;
import com.application.arenda.entities.announcements.OnApiListener;
import com.application.arenda.entities.announcements.viewAnnouncement.AdapterViewPager;
import com.application.arenda.entities.announcements.viewAnnouncement.ModelViewPager;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelPicture;
import com.application.arenda.entities.models.SharedViewModels;
import com.application.arenda.entities.recyclerView.OnItemClick;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.utils.glide.GlideUtils;
import com.application.arenda.entities.utils.retrofit.CodeHandler;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.mainWorkspace.activities.ActivityViewImages;
import com.application.arenda.R;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.calendarView.CalendarRentPeriod;
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments;
import com.application.arenda.ui.widgets.viewPager.PictureViewer;
import com.application.arenda.ui.widgets.horizontalList.HorizontalList;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FragmentViewAnnouncement extends Fragment implements AdapterActionBar {

    @Nullable
    @BindView(R.id.dotsLayout)
    LinearLayout dotsLayout;

    @Nullable
    @BindView(R.id.imgViewPager)
    PictureViewer imgViewPager;

    @Nullable
    @BindDrawable(R.drawable.ic_dot_selected)
    Drawable dotSelected;

    @Nullable
    @BindDrawable(R.drawable.ic_dot_unselected)
    Drawable dotUnselected;

    @Nullable
    @BindView(R.id.textNameProduct)
    TextView textNameProduct;

    @Nullable
    @BindView(R.id.textCostProduct)
    TextView textCostProduct;

    @Nullable
    @BindView(R.id.itemLocation)
    TextView textAddress;

    @Nullable
    @BindView(R.id.textPlacementDate)
    TextView textPlacementDate;

    @Nullable
    @BindView(R.id.userCardLogo)
    ImageView userCardLogo;

    @Nullable
    @BindView(R.id.userCardLogin)
    TextView userCardLogin;

    @Nullable
    @BindView(R.id.userCardRating)
    TextView userCardRating;

    @Nullable
    @BindView(R.id.userCardCountAnnouncements)
    TextView userCardCountAnnouncements;

    @Nullable
    @BindView(R.id.userCardUserCreated)
    TextView userCardUserCreated;

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

    @BindView(R.id.btnBooking)
    Button btnBooking;

    @Nullable
    @BindView(R.id.viewAnnouncementContainer)
    ConstraintLayout viewAnnouncementContainer;

    @Nullable
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Nullable
    @BindView(R.id.landLordAnnouncements)
    HorizontalList landLordAnnouncements;

    @Nullable
    @BindView(R.id.similarAnnouncements)
    HorizontalList similarAnnouncements;

    @BindView(R.id.bookingCalendar)
    CalendarRentPeriod bookingCalendar;

    @BindView(R.id.progressBarViewAnnouncement)
    ProgressBar progressBar;

    private ImageButton itemBtnBack, itemPhone,
            itemMessage, itemMore;

    private Unbinder unbinder;
    private AdapterViewPager adapterViewPager;
    private ViewPager.OnPageChangeListener pageListener;

    private ApiAnnouncement api;
    private String userToken = null;

    private LocalCacheManager cacheManager;

    private List<String> phoneNumbers = new ArrayList<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private Consumer<Boolean> favoriteSubscriber;
    private OnApiListener listenerFavoriteInsert;
    private LandLord_Similar_AnnouncementsAdapter landLordAnnouncementsAdapter;
    private LandLord_Similar_AnnouncementsAdapter similarAnnouncementsAdapter;

    private OnItemClick landLordItemClick;
    private OnItemClick similarItemClick;
    private OnItemClick landLordSimilarItemHeartClick;

    private LinearLayoutManager rvLandLordLayout;
    private LinearLayoutManager rvSimilarLayout;

    private SingleObserver<Boolean> subscriberFavoriteClick;

    private SharedViewModels sharedViewModels;

    private ContainerFragments containerFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_announcement, container, false);
        unbinder = ButterKnife.bind(this, view);

        api = ApiAnnouncement.getInstance();

        initComponents();

        return view;
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void initComponents() {
        containerFragments = ContainerFragments.getInstance(getContext());

        sharedViewModels = new ViewModelProvider(requireActivity()).get(SharedViewModels.class);

        rvLandLordLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        landLordAnnouncements.initRV(rvLandLordLayout);

        rvSimilarLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        similarAnnouncements.initRV(rvSimilarLayout);

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

        cacheManager = LocalCacheManager.getInstance(getContext());

        disposable.add(cacheManager
                .users()
                .getActiveUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(modelUsers -> {
                    if (modelUsers.size() > 0)
                        userToken = modelUsers.get(0).getToken();
                    else
                        userToken = null;

                    initInterfaces();

                    initListeners();

                    loadDataFromCache();
                }));

        landLordAnnouncementsAdapter = new LandLord_Similar_AnnouncementsAdapter();
        similarAnnouncementsAdapter = new LandLord_Similar_AnnouncementsAdapter();
    }

    public void initInterfaces() {
        subscriberFavoriteClick = new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(Boolean isFavorite) {
                selectHeart(isFavorite);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };

        landLordItemClick = (viewHolder, model) -> {
            ModelAnnouncement announcement = (ModelAnnouncement) model;

            sharedViewModels.selectLandLordAnnouncement(announcement);

            scrollView.smoothScrollTo(0, 0);

            inflateUI(announcement);

            initLandLordAnnouncements(announcement.getIdUser());
            initSimilarAnnouncements(announcement.getID(), announcement.getIdSubcategory());

            loadPeriodRentAnnouncement(announcement.getID());

            setViewerAnnouncement(announcement.getID());
        };

        similarItemClick = (viewHolder, model) -> {
            ModelAnnouncement announcement = (ModelAnnouncement) model;

            sharedViewModels.selectSimilarAnnouncement(announcement);

            scrollView.smoothScrollTo(0, 0);

            inflateUI(announcement);

            initLandLordAnnouncements(announcement.getIdUser());
            initSimilarAnnouncements(announcement.getID(), announcement.getIdSubcategory());

            loadPeriodRentAnnouncement(announcement.getID());

            setViewerAnnouncement(announcement.getID());
        };

        landLordSimilarItemHeartClick = (viewHolder, model) ->
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
                                ((LandLord_Similar_AnnouncementsVH) viewHolder).setActiveHeart(isFavorite);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                            }
                        });
    }

    private void initListeners() {
        landLordAnnouncements.seeAllClickListener(v -> {
            sharedViewModels.setLandLordAnnouncements(landLordAnnouncementsAdapter.getCollection());
            containerFragments.open(new FragmentViewAllLandLordAnnouncements());
        });

        similarAnnouncements.seeAllClickListener(v -> {
            sharedViewModels.setSimilarAnnouncements(similarAnnouncementsAdapter.getCollection());
            containerFragments.open(new FragmentViewAllSimilarAnnounements());
        });

        btnBooking.setOnClickListener(v -> {
            if (scrollView.getScrollY() != bookingCalendar.getY()) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, (int) bookingCalendar.getY()));
            }
        });
    }

    @SuppressLint("CheckResult")
    private void loadDataFromCache() {
        setProgress(true);

        sharedViewModels.getSelectedAnnouncement()
                .observe(getViewLifecycleOwner(), announcement -> {

                    inflateUI(announcement);

                    initLandLordAnnouncements(announcement.getIdUser());
                    initSimilarAnnouncements(announcement.getID(), announcement.getIdSubcategory());

                    setViewerAnnouncement(announcement.getID());

                    loadPeriodRentAnnouncement(announcement.getID());

                    setProgress(false);
                });
    }

    @SuppressLint("SetTextI18n")
    private void inflateUI(ModelAnnouncement announcement) {

        phoneNumbers.clear();

        phoneNumbers.add(announcement.getPhone_1());
        phoneNumbers.add(announcement.getPhone_2());
        phoneNumbers.add(announcement.getPhone_3());

        textPlacementDate.setText(Utils.getFormatingDate(getContext(), announcement.getAnnouncementCreated()));
        textNameProduct.setText(announcement.getName());

        //будет браться стоимость в зависимости от настроек по умолчанию
        textCostProduct.setText(announcement.getCostToBYN() + " руб./ч.");

        textAddress.setText(announcement.getAddress());
        textRating.setText(String.valueOf(announcement.getAnnouncementRating()));
        textCountRent.setText(String.valueOf(announcement.getCountRent()));

        textDescriptionProduct.setText(announcement.getDescription());

        GlideUtils.loadAvatar(getContext(), Uri.parse(announcement.getUserLogo()), userCardLogo);
        userCardLogin.setText(announcement.getLogin());
        userCardRating.setText(String.valueOf(announcement.getUserRating()));
        userCardCountAnnouncements.setText(String.valueOf(announcement.getCountAnnouncementsUser()));
        userCardUserCreated.setText(Utils.getFormatingDate(getContext(), announcement.getUserCreated(), Utils.DatePattern.dd_MM_yyyy));

        btnInsertToFavorite.setOnClickListener(v -> onClickFavorite(announcement.getID()));

        selectHeart(announcement.isFavorite());

        ModelPicture.convertToUris(announcement.getPictures())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSingleObserver<List<Uri>>() {
                    @Override
                    public void onSuccess(List<Uri> uriList) {
                        initViewPager(uriList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    private void setViewerAnnouncement(long idAnnouncement) {
        api.insertViewer(userToken, idAnnouncement, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void loadPeriodRentAnnouncement(long idAnnouncement) {
        api.loadPeriodRentAnnouncement(getContext(), idAnnouncement, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((modelPeriodRents) -> bookingCalendar.replaceEvents(new ArrayList<>(modelPeriodRents)), Timber::e);
    }

    private void initLandLordAnnouncements(long idLandLord) {
        landLordAnnouncementsAdapter.setItemViewClick(landLordItemClick);

        landLordAnnouncementsAdapter.setItemHeartClick(landLordSimilarItemHeartClick);

        api.loadLandLordAnnouncements(getContext(), userToken, idLandLord, 0, 10, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ModelAnnouncement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<ModelAnnouncement> modelAnnouncements) {
                        if (modelAnnouncements.size() > 0) {
                            landLordAnnouncements.setVisibility(View.VISIBLE);

                            landLordAnnouncementsAdapter.rewriteCollection(modelAnnouncements);

                            landLordAnnouncements.initAdapter(landLordAnnouncementsAdapter);
                        } else {
                            landLordAnnouncements.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        landLordAnnouncements.setVisibility(View.GONE);

                        Timber.e(e);
                    }
                });
    }

    private void initSimilarAnnouncements(long idAnnouncement, long idSubcategory) {
        similarAnnouncementsAdapter.setItemViewClick(similarItemClick);

        similarAnnouncementsAdapter.setItemHeartClick(landLordSimilarItemHeartClick);

        api.loadSimilarAnnouncements(getContext(), userToken, idSubcategory, idAnnouncement, 10, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ModelAnnouncement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<ModelAnnouncement> modelAnnouncements) {
                        if (modelAnnouncements.size() > 0) {
                            similarAnnouncements.setVisibility(View.VISIBLE);

                            similarAnnouncementsAdapter.rewriteCollection(modelAnnouncements);

                            similarAnnouncements.initAdapter(similarAnnouncementsAdapter);
                        } else {
                            similarAnnouncements.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        similarAnnouncements.setVisibility(View.GONE);

                        Timber.e(e);
                    }
                });
    }

    private void setProgress(boolean b) {
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
            viewAnnouncementContainer.setVisibility(View.INVISIBLE);
            btnBooking.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            viewAnnouncementContainer.setVisibility(View.VISIBLE);
            btnBooking.setVisibility(View.VISIBLE);
        }
    }

    public void onClickFavorite(long idAnnouncement) {
        api.insertToFavorite(userToken, idAnnouncement, listenerFavoriteInsert)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberFavoriteClick);
    }

    public void selectHeart(boolean b) {
        if (b)
            btnInsertToFavorite.setImageDrawable(getContext().getDrawable(R.drawable.ic_heart_selected));
        else
            btnInsertToFavorite.setImageDrawable(getContext().getDrawable(R.drawable.ic_heart_not_selected));
    }

    private void initViewPager(List<Uri> uris) {
        adapterViewPager = new AdapterViewPager(new ModelViewPager() {
            @Override
            public List<Uri> getCollectionUri() {
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

        Intent activityViewImages = new Intent(getActivity(), ActivityViewImages.class);

        adapterViewPager.setOnClickListener((uriList, selectedUri) -> {
            activityViewImages.putParcelableArrayListExtra("CollectionUri", (ArrayList<? extends Parcelable>) uriList);
            activityViewImages.putExtra("SelectedUri", selectedUri);

            startActivity(activityViewImages);
        });

        imgViewPager.setAdapter(adapterViewPager);
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

    @SuppressLint("MissingPermission")
    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemBtnBack.setOnClickListener(v -> getActivity().onBackPressed());

        itemPhone.setOnClickListener(v -> {
            if (phoneNumbers.size() > 0) {
                String[] array = new String[phoneNumbers.size()];

                for (int i = 0; i < phoneNumbers.size(); i++) {
                    array[i] = phoneNumbers.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle(R.string.dialog_phone_number_title);
                builder.setItems(array, (dialog, which) -> Dexter
                        .withContext(getContext())
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                getActivity().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumbers.get(which))));
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                getActivity().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumbers.get(which))));
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                            }
                        })
                        .check());

                builder.create();

                builder.show();
            } else
                Utils.messageOutput(getContext(), getContext().getResources().getString(R.string.dialog_phone_number_not_found));
        });
        itemMessage.setOnClickListener(v -> Utils.messageOutput(getContext(), "message"));
        itemMore.setOnClickListener(v -> Utils.messageOutput(getContext(), "more"));
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}