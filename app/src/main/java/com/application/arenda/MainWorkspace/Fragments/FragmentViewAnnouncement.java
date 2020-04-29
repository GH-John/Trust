package com.application.arenda.MainWorkspace.Fragments;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.Entities.Announcements.ApiAnnouncement;
import com.application.arenda.Entities.Announcements.OnApiListener;
import com.application.arenda.Entities.Announcements.ViewAnnouncement.AdapterViewPager;
import com.application.arenda.Entities.Announcements.ViewAnnouncement.ModelViewPager;
import com.application.arenda.Entities.Models.ModelAnnouncement;
import com.application.arenda.Entities.Models.ModelPicture;
import com.application.arenda.Entities.Models.ModelViewAnnouncement;
import com.application.arenda.Entities.Room.LocalCacheManager;
import com.application.arenda.Entities.Utils.Glide.GlideUtils;
import com.application.arenda.Entities.Utils.Retrofit.CodeHandler;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.MainWorkspace.Activities.ActivityViewImages;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;
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
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class FragmentViewAnnouncement extends Fragment implements AdapterActionBar {

    @SuppressLint("StaticFieldLeak")
    private static FragmentViewAnnouncement instance;

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

    @Nullable
    @BindView(R.id.progressBarVA)
    ProgressBar progressBar;

    @Nullable
    @BindView(R.id.viewAnnouncementContainer)
    ConstraintLayout viewAnnouncementContainer;

    private ImageView itemBtnBack, itemPhone,
            itemMessage, itemMore;

    private Unbinder unbinder;
    private AdapterViewPager adapterViewPager;
    private ViewPager.OnPageChangeListener pageListener;

    private ApiAnnouncement api;
    private String userToken = null;

    private LocalCacheManager cacheManager;

    private List<String> phoneNumbers = new ArrayList<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private Consumer<ModelViewAnnouncement> subscriber;
    private Consumer<Boolean> favoriteSubscriber;
    private OnApiListener listenerFavoriteInsert;

    private FragmentViewAnnouncement() {
    }

    public static FragmentViewAnnouncement getInstance() {
        if (instance == null)
            instance = new FragmentViewAnnouncement();

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_announcement, container, false);
        unbinder = ButterKnife.bind(this, view);

        api = ApiAnnouncement.getInstance();

        initComponents();

        Bundle bundle = getArguments();

        loadData(bundle != null ? bundle.getLong("idAnnouncement") : 0);

        return view;
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void initComponents() {
        listenerFavoriteInsert = new OnApiListener() {
            @Override
            public void onComplete(@NonNull CodeHandler code) {
                if (code.equals(CodeHandler.USER_NOT_FOUND)) {
                    Utils.messageOutput(getContext(), getResources().getString(R.string.warning_login_required));
                } else {
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
                }));

        subscriber = viewAnnouncement -> {
            ModelAnnouncement announcement = viewAnnouncement.getAnnouncement();

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
            userCardUserCreated.setText(Utils.getFormatingDate(getContext(), announcement.getUserCreated(), Utils.DatePattern.DATE_PATTERN_dd_MM_yyyy));

            btnInsertToFavorite.setOnClickListener(v -> onClickFavorite(announcement.getID()));

            selectHeart(announcement.isFavorite());

            cacheManager.pictures()
                    .getPictures(announcement.getID())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pictures -> {
                        Timber.tag("COUNT_PICTURES").d(String.valueOf(pictures.size()));

                        ModelPicture.convertToUris(pictures)
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

                        setProgress(false);
                    });
        };
    }

    private void setProgress(boolean b) {
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
            viewAnnouncementContainer.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            viewAnnouncementContainer.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("CheckResult")
    private void loadData(long idAnnouncement) {
        setProgress(true);

        disposable.add(cacheManager
                .announcements()
                .getViewAnnouncement(idAnnouncement)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void onClickFavorite(long idAnnouncement) {
        api.insertToFavorite(userToken, idAnnouncement, listenerFavoriteInsert)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
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
                });
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